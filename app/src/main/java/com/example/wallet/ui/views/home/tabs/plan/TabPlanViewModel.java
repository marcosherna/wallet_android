package com.example.wallet.ui.views.home.tabs.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.firebase.repository.PlanRepository;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class TabPlanViewModel extends ViewModel {
    final PlanRepository planRepository;
    final MutableLiveData<List<PlanSummaryUI>> plansWithSummary;
    public LiveData<List<PlanSummaryUI>> getPlansWithSummary() { return this.plansWithSummary; }
    public boolean isLoadData = false;

    public TabPlanViewModel(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.planRepository = new PlanRepository(userId);
        this.plansWithSummary = new MutableLiveData<>(new ArrayList<>());
    }

    public Completable initialize(){
        return Completable.create(emitter -> {
            planRepository.getAllPlans(new PlanRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(List<Plan> plans) {
                    // Convertir Plans a PlanSummaryUI
                    List<PlanSummaryUI> summaries = new ArrayList<>();
                    for (Plan p : plans) {
                        summaries.add(Mapper.PlanToSummaryUI(p));
                    }
                    plansWithSummary.postValue(summaries);
                    emitter.onComplete();
                }

                @Override
                public void onDataError(Exception e) {
                    emitter.onError(e);
                }

                @Override
                public void onDataFailed(String error) {
                    emitter.onError(new Exception(error));
                }
            });
        });
    }
}
