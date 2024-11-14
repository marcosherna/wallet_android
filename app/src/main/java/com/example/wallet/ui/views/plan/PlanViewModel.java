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
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlanViewModel extends ViewModel {
    final CompositeDisposable disposable = new CompositeDisposable();
    final
    PlanRepository planRepository;
    final List<PlanUI> lstCache;
    final MutableLiveData<List<PlanSummaryUI>> plansWithSummary;
    public LiveData<List<PlanSummaryUI>> getPlansWithSummary(){ return this.plansWithSummary; }
    final MutableLiveData<PlanUI> planSelected;
    public LiveData<PlanUI> getPlanSelected(){ return this.planSelected; }

    public boolean isLoadData = false;
    public PlanViewModel() {
        this.planRepository = new PlanRepository();
        this.planSelected = new MutableLiveData<>();
        this.lstCache = new ArrayList<>();
        this.plansWithSummary = new MutableLiveData<>(new ArrayList<>());
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

    public Completable deletePlan(PlanSummaryUI plan){
        return Completable.create(emitter -> {
            try{
                // TODO: implementar el metodo
                Thread.sleep(1000);

                this.loadPlans();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public Completable editPlan(PlanUI planUI){
        return Completable.create(emitter -> {
            try {
                // TODO: implementar metodo
                Thread.sleep(1000);

                this.loadPlans();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public void setPlanSelected(PlanSummaryUI planSelected){
        PlanUI select = this.lstCache.stream()
                .filter(p -> p.getId().equals(planSelected.getIdPlan()))
                .findFirst().orElse(null);

        this.planSelected.setValue(select);
    }

    private void loadPlans(){
        // TODO: implementar -> traer los datos reales
        if (this.planRepository != null){
            ArrayList<Plan> plans = this.planRepository.getAll();

            // para no hacer mas consultas
            this.lstCache.clear();
            List<PlanUI> planUIS = plans.stream()
                    .map(Mapper::PlanToUI).collect(Collectors.toList());
            this.lstCache.addAll(planUIS);

            List<PlanSummaryUI> planSummaryUIS = plans.stream()
                    .map(Mapper::PlanToSummaryUI).collect(Collectors.toList());

            this.plansWithSummary.postValue(planSummaryUIS);

        }
    }


}