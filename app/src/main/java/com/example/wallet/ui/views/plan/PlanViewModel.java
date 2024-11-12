package com.example.wallet.ui.views.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.PlanRepository;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class PlanViewModel extends ViewModel {
    PlanRepository planRepository;
    final MutableLiveData<HashMap<String, List<PlanSummaryUI>>> plansWithSummaries;
    public LiveData<HashMap<String, List<PlanSummaryUI>>> getPlansWithSummaries(){ return this.plansWithSummaries; }

    public boolean isLoadData = false;
    public PlanViewModel() {
        this.plansWithSummaries = new MutableLiveData<>(new HashMap<>());
        this.planRepository = new PlanRepository();
    }


    public Completable initialize(){
        return Completable.create(emitter -> {
            try {
                Thread.sleep(1000);
                this.loadPlans();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public Completable addPlan(PlanUI planUI){
        return Completable.create(emitter -> {
            try {
                // TODO: implemetar el metodo de agregar y actualizar
                Thread.sleep(1000);

                this.loadPlans();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    private void loadPlans(){
        // TODO: implementar -> traer los datos reales
        if (this.planRepository != null){
            ArrayList<Plan> plans = this.planRepository.getAll();
            HashMap<String, List<PlanSummaryUI>> _plansWithSummary = new HashMap<>();

            plans.forEach(plan -> {
                PlanSummaryUI planSummaryUI = Mapper.PlanToSummaryUI(plan);
                _plansWithSummary.put(plan.getName(), Collections.singletonList(planSummaryUI));
            });

            this.plansWithSummaries.postValue(_plansWithSummary);

        }
    }


}