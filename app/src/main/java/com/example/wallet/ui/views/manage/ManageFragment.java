package com.example.wallet.ui.views.manage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentManageBinding; import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.adapters.BDSFormMovementDialog;
import com.example.wallet.ui.adapters.RVAccountMovementWithCheck;

public class ManageFragment extends Fragment {

    FragmentManageBinding binding;
    NavController navController;
    BDSFormMovementDialog dialog;
    RVAccountMovementWithCheck rvAccountMovementWithCheckdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        dialog = new BDSFormMovementDialog();

        ManageViewModel manageViewModel = new ViewModelProvider(this).get(ManageViewModel.class);
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

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}