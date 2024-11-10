package com.example.wallet.ui.views.manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.fake.repository.PlanRepository;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageViewModel extends ViewModel {
    final PlanRepository planRepository;
    final AccountMovementRepository accountMovementRepository;
    final MutableLiveData<List<PlanUI>> plans;
    public LiveData<List<PlanUI>> getPlans() { return this.plans; }
    final MutableLiveData<List<AccountMovementUI>> movements;
    public LiveData<List<AccountMovementUI>> getMovements() { return this.movements; }
    public ManageViewModel(){
        this.planRepository = new PlanRepository();
        this.plans = new MutableLiveData<>(new ArrayList<>());
        this.accountMovementRepository = new AccountMovementRepository();
        this.movements = new MutableLiveData<>(new ArrayList<>());
        this.initialize();
    }

    public void initialize(){
        this.initializePlan();
        this.initializeMovement();
    }
    private void initializePlan(){
        List<PlanUI> plans = this.planRepository.getAll().stream()
                .map(Mapper::PlanToUI)
                .collect(Collectors.toList());
        this.plans.setValue(plans);
    }

    public void initializeMovement(){
        List<AccountMovementUI> movementUIS = this.accountMovementRepository.getAll().stream()
                .map(Mapper::AMovementToUI).collect(Collectors.toList());

        this.movements.setValue(movementUIS);
    }




}