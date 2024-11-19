package com.example.wallet.ui.views.manage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wallet.R;
import com.example.wallet.databinding.BottomSheetNewMovementBinding;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



public class BDSFormMovementDialog extends BottomSheetDialogFragment {
    public interface OnclickListener{
        void OnClick(AccountMovementUI movementUI);
    }
    AccountMovementUI movementUI;
    BottomSheetNewMovementBinding binding;
    List<PlanUI> plans;
    PlanUI planSelected;
    String title;
    OnclickListener listener;
    TypeAccountMovement typeAccountMovement;

    public BDSFormMovementDialog() {
        this.plans = new ArrayList<>();
        this.movementUI = null;
    }

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }

    public void setPlans(List<PlanUI> plans) {
        this.plans = plans;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMovementUI(AccountMovementUI movementUI) {
        this.movementUI = movementUI;
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

        if(!Objects.isNull(this.title) && !this.title.isEmpty()){
            this.binding.tvTitle.setText(this.title);
        }

        List<String> plansNames = this.plans.stream()
                .map(PlanUI::getName).collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_item,
                plansNames);

        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        this.binding.spinnerPlans.setAdapter(adapter);

        this.binding.spinnerPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                planSelected = plans.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        this.binding.cbTypeMovement.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.txtTypeMovement.setText(isChecked ? "Ingreso" : "Egreso")
        );

        this.binding.btnSaveMovement.setOnClickListener(__ -> {
            if (listener != null) {
                String idPlan = planSelected != null ? planSelected.getId() : "";
                String amount = this.binding.edtAmount.getText().toString();
                typeAccountMovement = binding.cbTypeMovement.isChecked() ? TypeAccountMovement.REVENUE : TypeAccountMovement.EXPENSE;
                this.movementUI = new AccountMovementUI("", amount, new Date().toString(), idPlan, typeAccountMovement);
                listener.OnClick(this.movementUI);
            }
        });
    }

    public void clearForm(){
        binding.edtAmount.setText("");
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (this.movementUI != null) {

            this.binding.edtAmount.setText(this.movementUI.getAmount());

            this.binding.cbTypeMovement.setChecked(this.movementUI.getTypeAccountMovement() == TypeAccountMovement.REVENUE);
            this.binding.txtTypeMovement.setText(this.movementUI.getTypeAccountMovement() == TypeAccountMovement.REVENUE ? "Ingreso" : "Egreso");

            String planId = this.movementUI.getIdPlan();
            int position = -1;
            for (int i = 0; i < plans.size(); i++) {
                if (plans.get(i).getId().equals(planId)) {
                    position = i;
                    break;
                }
            }
            if (position != -1) {
                this.binding.spinnerPlans.setSelection(position);
                planSelected = plans.get(position);
            }
            this.movementUI = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
