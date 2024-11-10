package com.example.wallet.ui.adapters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wallet.databinding.BottomSheetNewMovementBinding;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanUI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



public class BDSFormMovementDialog extends BottomSheetDialogFragment {
    public interface OnclickListener{
        void OnClick(AccountMovementUI movementUI);
    }
    AccountMovementUI movementUI;
    BottomSheetNewMovementBinding binding;
    List<PlanUI> plans;
    PlanUI planSelected;
    OnclickListener listener;
    public BDSFormMovementDialog() {
        this.plans = new ArrayList<>();
    }

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }

    public void setplans(List<PlanUI> plans) {
        this.plans = plans;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetNewMovementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String amount = this.binding.edtAmount.getText().toString();
        this.binding.txtTypeMovement.setText("Ingreso");
        this.binding.cbTypeMovement.setChecked(true);
        
        List<String> plansNames = this.plans.stream()
                .map(PlanUI::getName).collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                plansNames);

        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);

        this.binding.spinnerPlans.setAdapter(adapter);

        this.binding.spinnerPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedPlan = parent.getItemAtPosition(position).toString();
                planSelected = plans.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        this.binding.cbTypeMovement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.txtTypeMovement.setText( isChecked ? "Ingreso" : "Egreso");

            }
        });


        this.binding.btnSaveMovement.setOnClickListener(__ -> {
            if(listener != null){
                String idPlan = planSelected != null ? planSelected.getId() : "";
                this.movementUI = new AccountMovementUI("", amount, new Date().toString(), idPlan);
                listener.OnClick(this.movementUI);
            }
        });


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
