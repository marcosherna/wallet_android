package com.example.wallet.ui.views.manage;

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
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentManageBinding;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.adapters.RVAccountMovementWithCheck;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.example.wallet.utils.LogErrorHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManageFragment extends Fragment {

    FragmentManageBinding binding;
    BDSFormMovementDialog dialog;
    RVAccountMovementWithCheck rvAccountMovementWithCheckAdapter;
    ManageViewModel manageViewModel;
    final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dialog = new BDSFormMovementDialog();
        manageViewModel = new ViewModelProvider(this).get(ManageViewModel.class);
        binding = FragmentManageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(this.manageViewModel != null && !this.manageViewModel.isLoadData){
            this.loadData();
            this.manageViewModel.isLoadData = true;
        }

        rvAccountMovementWithCheckAdapter = new RVAccountMovementWithCheck();
        binding.rvAddAccountMovement.setAdapter(rvAccountMovementWithCheckAdapter);
        rvAccountMovementWithCheckAdapter.setOnEditableClickListener(this::handlerEditableClick);

        binding.swpRefreshLayout.setOnRefreshListener(this::loadData);

        this.binding.cbMovementCheckAll
                .setOnCheckedChangeListener((buttonView, isChecked) ->
                        rvAccountMovementWithCheckAdapter.checkAll(isChecked));

        this.binding.fabDeleteAccountMovement.setOnClickListener( __ -> this.handlerClickFabButton());
        binding.btnNewMovement.setOnClickListener(__ -> this.handlerClickNewAccountButton());

        this.binding.spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFilterSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Opcional: Manejar el caso donde no hay selección
            }
        });

        if(this.manageViewModel != null){
            manageViewModel.getMovements()
                    .observe(getViewLifecycleOwner(),rvAccountMovementWithCheckAdapter::addMovement);
            manageViewModel.getPlans().observe(getViewLifecycleOwner(), planUIS -> dialog.setPlans(planUIS));
        }
    }
    private void handlerEditableClick(AccountMovementUI movementUI){
        try {
            BDSFormMovementDialog formEdit = new BDSFormMovementDialog();


            formEdit.setMovementUI(movementUI);
            formEdit.setTitle("Editar Movimiento");

            formEdit.setPlans(this.manageViewModel.getPlans().getValue());
            formEdit.show(getParentFragmentManager(), "BDSFormEditMovementDialog");
            formEdit.setListener(movementUI1 -> {
                
                if(!movementUI1.getAmount().isEmpty()){
                    LoadingDialogFragment loading = new LoadingDialogFragment();
                    loading.show(requireActivity().getSupportFragmentManager(), "loadingDialog");
                    disposable.add(
                            manageViewModel.updateMovement(movementUI1)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> {
                                                loading.dismiss();
                                                formEdit.clearForm();
                                                formEdit.dismiss();
                                                Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                                                formEdit.onDestroyView();
                                            },
                                            throwable -> {
                                                loading.dismiss();
                                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            }
                                    )
                    );
                } else {
                    Toast.makeText(getContext(), "Digita un monto", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            LogErrorHelper.print(e);
        }
    }

    private void handleFilterSelection(int position) {
        try {
            if (position != 0 && manageViewModel.getMovements().getValue() != null) {
                List<AccountMovementUI> cacheList = manageViewModel.getMovementUISCache();

                switch (position) {
                    case 1: // 1 -> Todos
                        rvAccountMovementWithCheckAdapter.addMovement(cacheList);
                        break;
                    case 2: // 2 -> Ingresos
                        manageViewModel.filterByType(TypeAccountMovement.REVENUE);
                        break;
                    case 3: // 3 -> Egresos
                        manageViewModel.filterByType(TypeAccountMovement.EXPENSE);
                        break;
                    case 4: // 4 -> Mas recientes
                        manageViewModel.getAllRecent();
                        break;
                }
            }
        } catch (Exception e) {
            Log.println(Log.ERROR, "methodFilterError", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void handlerClickNewAccountButton(){
        dialog.setListener( movementUI -> {
            if (movementUI.getAmount().isEmpty()){
                Toast.makeText(getContext(), "Digita un monto", Toast.LENGTH_SHORT).show();
            }else {
                LoadingDialogFragment loading = new LoadingDialogFragment();
                loading.show(requireActivity().getSupportFragmentManager(), "loadingDialogAdd");

                disposable.add(
                        manageViewModel.addMovements(movementUI)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> {
                                            loading.dismiss();
                                            dialog.clearForm();
                                            dialog.dismiss();
                                        },
                                        throwable -> {
                                            loading.dismiss();
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                )
                );
            }
        });

        dialog.show(getParentFragmentManager(), "BDSFormMovementDialog");
    }

    private void handlerClickFabButton(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        List<AccountMovementUI> selections = rvAccountMovementWithCheckAdapter.getItems().stream()
                .filter(AccountMovementUI::isCheck).collect(Collectors.toList());

        alert.setTitle("Advertencia");
        alert.setMessage("Los elementos seleccionados seran eliminados permanentemente.");

        if(selections.isEmpty()){
            Toast.makeText(getContext(), "Selecciona minimo uno", Toast.LENGTH_SHORT).show();
        }


        if(!selections.isEmpty()){

            alert.setPositiveButton( "Aceptar",
                    (dialog,which)-> this.executeDelete(selections));

            alert.setNegativeButton("Cancelar",
                    (dialog,which)-> dialog.dismiss());
            alert.show();
        }
    }

    private void loadData(){
        try {
            binding.swpRefreshLayout.setRefreshing(true);
            disposable.add(
                    manageViewModel.initialize()
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
        } catch (Exception e){
            Log.e("loadDataError -> ManageView", Objects.requireNonNull(e.getMessage()));
        }

    }

    // Ejecuta la eliminación de los elementos seleccionados
    private void executeDelete(List<AccountMovementUI> selections) {
        try {
            LoadingDialogFragment loading = new LoadingDialogFragment();
            loading.show(requireActivity().getSupportFragmentManager(), "loadingDialog");

            disposable.add(
                    manageViewModel.deletingMovements(selections)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        loading.dismiss();
                                        if(binding.cbMovementCheckAll.isChecked()){
                                            binding.cbMovementCheckAll.setChecked(false);
                                        }
                                    },
                                    throwable -> {
                                        loading.dismiss();
                                        if(binding.cbMovementCheckAll.isChecked()){
                                            binding.cbMovementCheckAll.setChecked(false);
                                        }
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                            )
            );
        } catch (Exception e){
           Log.e("DeleteError", Objects.requireNonNull(e.getMessage()));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }


}