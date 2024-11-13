package com.example.wallet.ui.views.home.tabs.plan;

import android.widget.AutoCompleteTextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.PlanRepository;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;

public class TabPlanViewModel extends ViewModel {
    final PlanRepository planRepository;
    final MutableLiveData<List<PlanSummaryUI>> plansWithSummary;
    public LiveData<List<PlanSummaryUI>> getPlansWithSummary (){ return this.plansWithSummary; }
    public boolean isLoadData = false;
    public TabPlanViewModel(){
        this.planRepository = new PlanRepository();
        this.plansWithSummary = new MutableLiveData<>(new ArrayList<>());
    }

    public Completable initialize(){
        return Completable.create(emitter -> {
            try {
                // TODO: Implementar la carga de los datos
                Thread.sleep(1000);
                this.loadPlans();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    private void loadPlans(){
        List<PlanSummaryUI> planUIS = this.planRepository.getAll().stream()
                .map(Mapper::PlanToSummaryUI).collect(Collectors.toList());

        this.plansWithSummary.postValue(planUIS);
    }
}