package com.example.wallet.ui.views.home.tabs.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.databinding.FragmentTabPlanBinding;
import com.example.wallet.ui.adapters.PlanExpandableListAdapter;
import com.example.wallet.ui.models.PlanSummaryUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabPlanFragment extends Fragment {
    private FragmentTabPlanBinding binding; // generado a partir de fragment_tab_plan
    private TabPlanViewModel viewModel;
    private PlanExpandableListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabPlanBinding.inflate(inflater, container, false);

        adapter = new PlanExpandableListAdapter(requireContext(), true);
        binding.rvAllSummaryPlans.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TabPlanViewModel.class);

        viewModel.initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        throwable -> {}
                );

        viewModel.getPlansWithSummary().observe(getViewLifecycleOwner(), planSummaries -> {
            // Aquí necesitas formatear planSummaries para actualizar el adapter
            // Por ejemplo, si tus planes no necesitan agruparse, puedes crear un grupo genérico
            // o agruparlos por algún criterio.

            // Ejemplo simple: un solo grupo con todos los planes
            List<String> titles = new ArrayList<>();
            titles.add("Mis Planes");

            HashMap<String, List<PlanSummaryUI>> map = new HashMap<>();
            map.put("Mis Planes", planSummaries);

            adapter.setTitlesPlans(titles);
            adapter.setPlansWithSummary(map);

            // Esto actualizará la vista del ExpandableListView
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
