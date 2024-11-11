package com.example.wallet.ui.views.manage;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentManageBinding;
import com.example.wallet.ui.adapters.BDSFormMovementDialog;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.adapters.RVAccountMovementWithCheck;
import com.example.wallet.ui.models.AccountMovementUI;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManageFragment extends Fragment {

    FragmentManageBinding binding;
    NavController navController;
    BDSFormMovementDialog dialog;
    RVAccountMovementWithCheck rvAccountMovementWithCheckdapter;
    ManageViewModel manageViewModel;
    final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        dialog = new BDSFormMovementDialog();

        manageViewModel = new ViewModelProvider(this).get(ManageViewModel.class);
        binding = FragmentManageBinding.inflate(inflater, container, false);

        rvAccountMovementWithCheckdapter = new RVAccountMovementWithCheck();
        binding.rvAddAccountMovement.setAdapter(rvAccountMovementWithCheckdapter);

        manageViewModel.getPlans().observe(getViewLifecycleOwner(), planUIS -> dialog.setplans(planUIS));

        binding.btnNewMovement.setOnClickListener(__ -> {

            dialog.setListener( movementUI -> {
                // TODO: implementar el metodo para agregar un nuevo movimiento
                Toast.makeText(getContext(), movementUI.toString(), Toast.LENGTH_SHORT).show();
            });

            dialog.show(getParentFragmentManager(), "BDSFormMovementDialog");
        });

        manageViewModel.getMovements()
                .observe(getViewLifecycleOwner(), movementUIS ->
                        movementUIS
                                .forEach(rvAccountMovementWithCheckdapter::addMovement));

        // Seleccionar todos
        this.binding.cbMovementCheckAll.setOnCheckedChangeListener((buttonView, isChecked) -> rvAccountMovementWithCheckdapter.checkAll(isChecked));

        // Se activa al click del boton flotante
        this.binding.fabDeleteAccountMovement.setOnClickListener( __ -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

            List<AccountMovementUI> selections = rvAccountMovementWithCheckdapter.getItems().stream()
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

        });

        return binding.getRoot();
    }

    // Ejecuta la eliminación de los elementos seleccionados
    private void executeDelete(List<AccountMovementUI> selections) {
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingDialog");

        disposable.add(
                manageViewModel.deletingMovements(selections)
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }


}