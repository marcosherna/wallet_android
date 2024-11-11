package com.example.wallet.ui.views.manage;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentManageBinding;
import com.example.wallet.ui.adapters.BDSFormMovementDialog;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.adapters.RVAccountMovementWithCheck;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.TypeAccountMovement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

        this.loadData();

        rvAccountMovementWithCheckdapter = new RVAccountMovementWithCheck();
        binding.rvAddAccountMovement.setAdapter(rvAccountMovementWithCheckdapter);
        binding.swpRefreshLayout.setOnRefreshListener(this::loadData);




        // muestara al dialog con el formulario para un nuevo movimiento
        binding.btnNewMovement.setOnClickListener(__ -> {

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
        });

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

        // Filtrar los datos
        this.binding.spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(position != 0 && manageViewModel.getMovements().getValue() != null){
                        List<AccountMovementUI> cacheList = manageViewModel.getMovementUISCache();

                        switch (position) {
                            case 1: { // 1 -> Todos
                                rvAccountMovementWithCheckdapter.addMovement(cacheList);
                            } break;
                            case 2: { // 2 -> Ingresos
                                manageViewModel.filterByType(TypeAccountMovement.REVENUE);
                            } break;
                            case 3: { // 3 -> Egresos
                                manageViewModel.filterByType(TypeAccountMovement.EXPENSE);;
                            } break;
                            case 4: { // 4 -> Mas recientes
                                manageViewModel.getAllRecent();
                            } break;
                        }


                    }
                } catch (Exception e){
                    Log.println(Log.ERROR,"EERR",  e.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Opcional: Manejar el caso donde no hay selección
            }
        });

        manageViewModel.getMovements()
                .observe(getViewLifecycleOwner(),rvAccountMovementWithCheckdapter::addMovement);

        manageViewModel.getPlans().observe(getViewLifecycleOwner(), planUIS -> dialog.setplans(planUIS));
        return binding.getRoot();
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
            Log.e("loadData", e.getMessage());
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
            Log.e("DeleteError", e.getMessage());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }


}