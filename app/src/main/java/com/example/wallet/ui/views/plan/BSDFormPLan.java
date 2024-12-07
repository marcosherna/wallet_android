package com.example.wallet.ui.views.plan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wallet.databinding.BottomSheetNewPlanBinding;
import com.example.wallet.ui.models.PlanUI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class BSDFormPLan extends BottomSheetDialogFragment {

    private PlanUI plan;
    String title;
    private BottomSheetNewPlanBinding binding;
    private OnSaveClickListener listener;

    public interface OnSaveClickListener {
        void onSaveClicked(PlanUI planUI);
    }

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlanSelected(PlanUI plan){
        this.plan = plan;
        if (binding != null) {
            this.updateViewWithPlanData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.binding = BottomSheetNewPlanBinding.inflate(inflater, container,false);
        return this.binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding.tvTitle.setText(title != null ? title : "Nuevo Plan");
        this.binding.edtTerm.setOnClickListener(__ -> showDatePickerDialog());

        this.updateViewWithPlanData();

        this.binding.btnSaveNewPlan.setOnClickListener(v -> {
            PlanUI newPlan =  new PlanUI();
            String namePlan =  this.binding.edtName.getText().toString().trim();
            String amountPlan = this.binding.edtAmount.getText().toString().trim();
            String termPlan =  this.binding.edtTerm.getText().toString().trim();
            String description = this.binding.edtDescription.getText().toString().trim();

            // Validaciones básicas
            if (namePlan.isEmpty()) {
                binding.edtName.setError("El nombre es requerido");
                return;
            }

            if (amountPlan.isEmpty()) {
                binding.edtAmount.setError("El monto es requerido");
                return;
            }

            if (termPlan.isEmpty()) {
                binding.edtTerm.setError("El plazo es requerido");
                return;
            }

            // Asignar los valores al objeto PlanUI
            newPlan.setName(namePlan);
            newPlan.setTargetAmount(amountPlan);
            newPlan.setPaymentDeadline(termPlan);
            newPlan.setDescription(description);

            if (listener != null) {
                listener.onSaveClicked(newPlan);
                dismiss(); // Cerrar el dialogo
            }

        });
    }

    private void updateViewWithPlanData() {
        if (plan != null) {
            binding.edtName.setText(plan.getName());
            binding.edtAmount.setText(plan.getTargetAmount());
            binding.edtTerm.setText(plan.getPaymentDeadline());
            binding.edtDescription.setText(plan.getDescription());
        }
    }

    public void clearData() {
        binding.edtName.setText("");
        binding.edtAmount.setText("");
        binding.edtTerm.setText("");
        binding.edtDescription.setText("");
        this.plan = null;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            binding.edtTerm.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
