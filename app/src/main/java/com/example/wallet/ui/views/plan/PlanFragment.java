package com.example.wallet.ui.views.plan;

import androidx.lifecycle.ViewModelProvider;

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
import com.example.wallet.ui.adapters.BSDFormPLan;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.adapters.PlanExpandableListAdapter;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlanFragment extends Fragment {

    FragmentPlanBinding binding;
    PlanViewModel planViewModel;
    BSDFormPLan adapterDialog;
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

        PlanExpandableListAdapter planExpandableListAdapter = new PlanExpandableListAdapter(getContext());
        binding.expandableListPlan.setAdapter(planExpandableListAdapter);

        planViewModel.getPlansWithSummaries().observe(getViewLifecycleOwner(), plansSummary -> {
            List<String> plansNames = new ArrayList<>(plansSummary.keySet());
            planExpandableListAdapter.setTitlesPlans(plansNames);
            planExpandableListAdapter.setPlansWithSummary(plansSummary);
        });

        binding.btnActionButton.setOnClickListener(__ -> {

            adapterDialog = new BSDFormPLan();
            adapterDialog.setTitle("Nuevo Plan");

            adapterDialog.setOnSaveClickListener( (planUI) -> {
                if(planUI.getName().isEmpty() ||
                        planUI.getDescription().isEmpty() ||
                        planUI.getTargetAmount().isEmpty() ||
                        planUI.getPaymentDeadline().isEmpty()){
                    Toast.makeText(getContext(), "Todos son requeridos", Toast.LENGTH_SHORT).show();
                } else {
                    this.executeAdd(planUI);
                }
            });
            adapterDialog.show(requireActivity().getSupportFragmentManager(), "Form");
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
            Log.println(Log.ERROR, "loadDataError -> PlanView", Objects.requireNonNull(e.getMessage()));
        }

    }

    private void executeAdd(PlanUI planUI){
        try {
            LoadingDialogFragment loading = new LoadingDialogFragment();
            loading.show(requireActivity().getSupportFragmentManager(), "loadingAddPlan");

            disposable.add(
                    planViewModel.addPlan(planUI)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        loading.dismiss();
                                        adapterDialog.dismiss();
                                    },
                                    throwable -> {
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                            )
            );
        } catch (Exception e){
            Log.println(Log.ERROR, "executeAddError", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }


}