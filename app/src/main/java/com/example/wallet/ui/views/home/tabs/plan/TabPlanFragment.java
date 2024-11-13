package com.example.wallet.ui.views.home.tabs.plan;

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

import com.example.wallet.databinding.FragmentTabPlanBinding;
import com.example.wallet.ui.adapters.PlanExpandableListAdapter;
import com.example.wallet.ui.models.PlanSummaryUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabPlanFragment extends Fragment {

    FragmentTabPlanBinding binding;
    TabPlanViewModel tabPlanViewModel;
    final CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tabPlanViewModel = new ViewModelProvider(this).get(TabPlanViewModel.class);
        binding = FragmentTabPlanBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlanExpandableListAdapter planExpandableListAdapter = new PlanExpandableListAdapter(getContext(), false);

        if(this.tabPlanViewModel != null && !this.tabPlanViewModel.isLoadData){
            this.loadData();
            this.tabPlanViewModel.isLoadData = true;
        }

        this.binding.rvAllSummaryPlans.setAdapter(planExpandableListAdapter);

        if(this.tabPlanViewModel != null){
            this.tabPlanViewModel.getPlansWithSummary().observe(getViewLifecycleOwner(), planSummary -> {
                HashMap<String, List<PlanSummaryUI>> summary = new HashMap<>();

                planSummary.forEach(plan ->
                        summary.put(plan.getNamePlan(), Collections.singletonList(plan)));

                List<String> name = new ArrayList<>(summary.keySet());
                planExpandableListAdapter.setTitlesPlans(name);
                planExpandableListAdapter.setPlansWithSummary(summary);

            });
        }

        this.binding.swpRefreshPlans.setOnRefreshListener(this::loadData);
    }

    private void loadData(){
        try {
            this.binding.swpRefreshPlans.setRefreshing(true);
            disposable.add(
                    this.tabPlanViewModel.initialize()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> this.binding.swpRefreshPlans.setRefreshing(false),
                                    throwable -> {
                                        this.binding.swpRefreshPlans.setRefreshing(false);
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                            )
            );
        } catch (Exception e){
            Log.println(Log.ERROR, "loadData -> tabPlansView", "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }

}