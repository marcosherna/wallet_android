package com.example.wallet.ui.adapters;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.wallet.databinding.BottomSheetNewPlanBinding;
import com.example.wallet.ui.base.OnSaveClickListener;
import com.example.wallet.ui.models.PlanUI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class BSDFormPLan extends BottomSheetDialogFragment {

    private PlanUI plan;
    String title;
    BottomSheetNewPlanBinding binding;
    private OnSaveClickListener listener;

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        this.listener = listener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = BottomSheetNewPlanBinding.inflate(inflater, container,false);
        return this.binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.binding.tvTitle.setText(title);
        this.binding.edtTerm.setOnClickListener(__ -> showDatePickerDialog());

        this.binding.btnSaveNewPlan.setOnClickListener(v -> {
            this.plan = this.plan != null ? this.plan : new PlanUI();
            String namePlan =  this.binding.edtName.getText().toString();
            String amountPlan = this.binding.edtAmount.getText().toString();
            String termPlan =  this.binding.edtTerm.getText().toString();
            String description = this.binding.edtDescription.getText().toString();

            this.plan.setName(namePlan);
            this.plan.setTargetAmount(amountPlan);
            this.plan.setPaymentDeadline(termPlan);
            this.plan.setDescription(description);

            if (listener != null) {
                listener.onSaveClicked(this.plan);
            }

        });
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
}
