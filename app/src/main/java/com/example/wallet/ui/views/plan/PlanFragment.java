package com.example.wallet.ui.views.plan;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentPlanBinding;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.adapters.PlanExpandableListAdapter;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanFragment extends Fragment {

    FragmentPlanBinding binding;
    PlanViewModel planViewModel;
    BSDFormPLan formAdd;
    BSDFormPLan formEdit;
    final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        binding = FragmentPlanBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(planViewModel != null && !this.planViewModel.isLoadData){
            this.loadData();
            this.planViewModel.isLoadData = true;
        }

//        formEdit = new BSDFormPLan();

        PlanExpandableListAdapter planExpandableListAdapter = new PlanExpandableListAdapter(getContext(), true);
        binding.expandableListPlan.setAdapter(planExpandableListAdapter);

        planExpandableListAdapter.setOnListenerNegativeClick(this::handlerDeleteItemClick);
        planExpandableListAdapter.setOnListenerPositiveClick(this::handlerEditItemClick);

        planViewModel.getPlansWithSummary().observe(getViewLifecycleOwner(), plansSummary -> {
            HashMap<String, List<PlanSummaryUI>> _plansWithSummary = new HashMap<>();

            plansSummary.forEach(plan -> _plansWithSummary
                    .put(plan.getNamePlan(), Collections.singletonList(plan)));

            List<String> plansNames = new ArrayList<>(_plansWithSummary.keySet());
            planExpandableListAdapter.setTitlesPlans(plansNames);
            planExpandableListAdapter.setPlansWithSummary(_plansWithSummary);
        });

        //planViewModel.getPlanSelected().observe(getViewLifecycleOwner(), formEdit::setPlanSelected);
        formAdd = new BSDFormPLan();
        binding.btnActionButton.setOnClickListener(__ -> {
            formAdd.setTitle("Nuevo Plan");
            formAdd.setOnSaveClickListener( (planUI) -> {
                if(planUI.getName().isEmpty() ||
                        planUI.getDescription().isEmpty() ||
                        planUI.getTargetAmount().isEmpty() ||
                        planUI.getPaymentDeadline().isEmpty()){
                    Toast.makeText(getContext(), "Todos son requeridos", Toast.LENGTH_SHORT).show();
                } else {
                    this.executeAdd(planUI);
                }
            });

            formAdd.show(requireActivity().getSupportFragmentManager(), "Form");
        });

        binding.swpRefreshLayout.setOnRefreshListener(this::loadData);

    }

    private void loadData(){
        try {
            if (planViewModel != null){
                binding.swpRefreshLayout.setRefreshing(true);
                disposable.add(
                        planViewModel.initialize()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> binding.swpRefreshLayout.setRefreshing(false),
                                        throwable -> {
                                            binding.swpRefreshLayout.setRefreshing(false);
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                )
                );
            }

        } catch (Exception e){
            Log.println(Log.ERROR, "loadDataError -> PlanView", "");
        }
    }

    private void handlerDeleteItemClick(PlanSummaryUI selection){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Advertencia");
        alert.setMessage("El elemento sera eliminado permanentemente.");

        try {
            alert.setPositiveButton( "Aceptar", (dialog,which)-> {
                dialog.dismiss();
                this.executeDeletion(selection);
            });

            alert.setNegativeButton("Cancelar",
                    (dialog,which)-> dialog.dismiss());

            alert.show();
        } catch (Exception e){
            Log.println(Log.ERROR,"deletedItemError", "");
        }
    }

    private void executeDeletion(PlanSummaryUI selection){
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingDeletePlan");

        disposable.add(
                planViewModel.deletePlan(selection)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                loading::dismiss,
                                throwable -> {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private void handlerEditItemClick(PlanSummaryUI selection){
        try {
            formEdit = new BSDFormPLan();
            formEdit.setTitle("Editar Plan");
            planViewModel.setPlanSelected(selection);
            formEdit.setPlanSelected(planViewModel.getPlanSelected().getValue());

            formEdit.setOnSaveClickListener(this::executeEdit);

            formEdit.show(requireActivity().getSupportFragmentManager(), "FormEdit");
        } catch (Exception e){
            Log.println(Log.ERROR, "executeHandlerSelectedError", "");
        }
    }

    private void executeEdit(PlanUI planUI){
        if(planUI.getName().isEmpty() ||
                planUI.getDescription().isEmpty() ||
                planUI.getTargetAmount().isEmpty() ||
                planUI.getPaymentDeadline().isEmpty()){
            Toast.makeText(getContext(), "Todos son requeridos", Toast.LENGTH_SHORT).show();
        } else {
            LoadingDialogFragment loading = new LoadingDialogFragment();
            loading.show(requireActivity().getSupportFragmentManager(), "LoadingEditPlan");
            disposable.add(
                    planViewModel.editPlan(planUI)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        loading.dismiss();
                                        formEdit.clearData();
                                        formEdit.dismiss();
                                    },
                                    throwable -> {
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                            )
            );
        }
    }

    private void executeAdd(PlanUI planUI){
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingAddPlan");
        try {
            disposable.add(
                    planViewModel.addPlan(planUI)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        loading.dismiss();
                                        formAdd.clearData();
                                        formAdd.dismiss();
                                    },
                                    throwable -> {
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                            )
            );

        } catch (Exception e){
            loading.dismiss();
            Log.println(Log.ERROR, "executeAddError", "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }


}