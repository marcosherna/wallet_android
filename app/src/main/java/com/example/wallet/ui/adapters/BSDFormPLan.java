package com.example.wallet.ui.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wallet.R;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.base.OnSaveClickListener;
import com.example.wallet.ui.models.PlanUI;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BSDFormPLan extends BottomSheetDialogFragment {

    private PlanUI plan;
    String title;
    private OnSaveClickListener listener;

    public void setOnSaveClickListener(OnSaveClickListener listener) {
        this.listener = listener;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_new_plan, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        EditText etNamePlan = view.findViewById(R.id.edtName);
        EditText etAmountPlan = view.findViewById(R.id.edtAmount);
        EditText etTermPlan = view.findViewById(R.id.edtTerm);
        EditText etDescription = view.findViewById(R.id.edtDescription);
        Button btnSave = view.findViewById(R.id.btnSaveNewPlan);

        tvTitle.setText(title);

        btnSave.setOnClickListener(v -> {
            this.plan = this.plan != null ? this.plan : new PlanUI();
            String namePlan = ((EditText) view.findViewById(R.id.edtName)).getText().toString();
            String amountPlan = ((EditText) view.findViewById(R.id.edtAmount)).getText().toString();
            String termPlan = ((EditText) view.findViewById(R.id.edtTerm)).getText().toString();
            String description = ((EditText) view.findViewById(R.id.edtDescription)).getText().toString();

            this.plan.setName(namePlan);
            this.plan.setTargetAmount(amountPlan);
            this.plan.setPaymentDeadline(termPlan);
            this.plan.setDescription(description);

            if (listener != null) {
                listener.onSaveClicked(this.plan);
            }

        });
    }
}
