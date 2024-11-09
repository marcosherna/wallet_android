package com.example.wallet.ui.views.manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.PlanRepository;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageViewModel extends ViewModel {
    final PlanRepository planRepository;
    final MutableLiveData<List<PlanUI>> plans;
    public LiveData<List<PlanUI>> getPlans() { return this.plans; }
    public ManageViewModel(){
        this.planRepository = new PlanRepository();
        this.plans = new MutableLiveData<>(new ArrayList<>());
        this.initialize();
    }

    public void initialize(){
        this.initializePlan();
    }

    private void initializePlan(){
        List<PlanUI> plans = this.planRepository.getAll().stream()
                .map(Mapper::PlanToUI)
                .collect(Collectors.toList());
        this.plans.setValue(plans);
    }




}