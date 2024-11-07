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

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentPlanBinding;
import com.example.wallet.domain.fake.date.ExpandableListDataPump;
import com.example.wallet.ui.adapters.BSDFormPLan;
import com.example.wallet.ui.adapters.CustomExpandableListAdapter;
import com.example.wallet.ui.adapters.PlanExpandableListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlanFragment extends Fragment {

    FragmentPlanBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PlanViewModel planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        binding = FragmentPlanBinding.inflate(inflater, container, false);

        PlanExpandableListAdapter planExpandableListAdapter = new PlanExpandableListAdapter(getContext());
        binding.expandableListPlan.setAdapter(planExpandableListAdapter);

        planViewModel.getPlansWithSumary().observe(getViewLifecycleOwner(), plansSumary -> {
            List<String> plansNames = new ArrayList<>(plansSumary.keySet());
            planExpandableListAdapter.setTitlesPlans(plansNames);
            planExpandableListAdapter.setPlansWithSumary(plansSumary);
        });

        binding.btnActionButton.setOnClickListener(__ -> {

            BSDFormPLan adapterDialog = new BSDFormPLan();
            adapterDialog.setTitle("Nuevo Plan");

            adapterDialog.setOnSaveClickListener( (planUI) -> {
                boolean result = planViewModel.addPlan(planUI);
                Toast.makeText(getContext(), result? "Guardado": "Error", Toast.LENGTH_SHORT).show();
            });
            adapterDialog.show(getActivity().getSupportFragmentManager(), "Form");
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}