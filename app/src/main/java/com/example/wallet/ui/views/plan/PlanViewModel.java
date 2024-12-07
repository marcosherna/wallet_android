package com.example.wallet.ui.views.plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.firebase.repository.PlanRepository;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PlanViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final PlanRepository planRepository;
    private final List<Plan> lstCache; // Lista de planes de dominio
    private final MutableLiveData<List<PlanSummaryUI>> plansWithSummary;
    public LiveData<List<PlanSummaryUI>> getPlansWithSummary() { return this.plansWithSummary; }
    private final MutableLiveData<PlanUI> planSelected;
    public LiveData<PlanUI> getPlanSelected() { return this.planSelected; }
    private final MutableLiveData<String> errorMessage;
    public LiveData<String> getErrorMessage() { return this.errorMessage; }

    public boolean isLoadData = false;

    public PlanViewModel() {
        // Obtener el usuario actual de FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            this.planRepository = new PlanRepository(uid);
        } else {
            // Manejar el caso donde el usuario no está autenticado
            // Puedes lanzar una excepción o manejarlo de otra manera según tu lógica
            throw new IllegalStateException("Usuario no autenticado");
        }

        this.planSelected = new MutableLiveData<>();
        this.lstCache = new ArrayList<>();
        this.plansWithSummary = new MutableLiveData<>(new ArrayList<>());
        this.errorMessage = new MutableLiveData<>();
    }


    public Completable initialize() {
        return Completable.create(emitter -> {
            try {
                this.loadPlans();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }


    public Completable addPlan(PlanUI planUI) {
        return Completable.create(emitter -> {
            try {
                // Convertir PlanUI a Plan usando el Mapper
                Plan plan = Mapper.PlanUIToPlan(planUI);

                this.planRepository.savePlan(plan, new PlanRepository.OnDataLoadedListener() {
                    @Override
                    public void onDataLoaded(List<Plan> plans) {
                        loadPlans();
                        emitter.onComplete();
                    }

                    @Override
                    public void onDataError(Exception e) {
                        errorMessage.postValue(e.getMessage());
                        emitter.onError(e);
                    }

                    @Override
                    public void onDataFailed(String error) {
                        errorMessage.postValue(error);
                        emitter.onError(new Exception(error));
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    public Completable deletePlan(PlanSummaryUI planSummaryUI) {
        return Completable.create(emitter -> {
            try {
                this.planRepository.deletePlan(planSummaryUI.getIdPlan(), new PlanRepository.OnDataLoadedListener() {
                    @Override
                    public void onDataLoaded(List<Plan> plans) {
                        loadPlans();
                        emitter.onComplete();
                    }

                    @Override
                    public void onDataError(Exception e) {
                        errorMessage.postValue(e.getMessage());
                        emitter.onError(e);
                    }

                    @Override
                    public void onDataFailed(String error) {
                        errorMessage.postValue(error);
                        emitter.onError(new Exception(error));
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }


    public Completable editPlan(PlanUI planUI) {
        return Completable.create(emitter -> {
            try {
                // Convertir PlanUI a Plan usando el Mapper
                Plan plan = Mapper.PlanUIToPlan(planUI);

                this.planRepository.savePlan(plan, new PlanRepository.OnDataLoadedListener() {
                    @Override
                    public void onDataLoaded(List<Plan> plans) {
                        loadPlans();
                        emitter.onComplete();
                    }

                    @Override
                    public void onDataError(Exception e) {
                        errorMessage.postValue(e.getMessage());
                        emitter.onError(e);
                    }

                    @Override
                    public void onDataFailed(String error) {
                        errorMessage.postValue(error);
                        emitter.onError(new Exception(error));
                    }
                });
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }


    public void setPlanSelected(PlanSummaryUI planSummaryUI) {
        Plan selectedPlan = this.lstCache.stream()
                .filter(p -> p.getId().equals(planSummaryUI.getIdPlan()))
                .findFirst().orElse(null);

        if (selectedPlan != null) {
            PlanUI selectUI = Mapper.PlanToUI(selectedPlan);
            this.planSelected.setValue(selectUI);
        } else {
            this.planSelected.setValue(null);
        }
    }


    private void loadPlans() {
        if (this.planRepository != null) {
            this.planRepository.getAllPlans(new PlanRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(List<Plan> plans) {
                    // Actualizar la caché y el LiveData
                    lstCache.clear();
                    lstCache.addAll(plans);

                    List<PlanSummaryUI> planSummaryUIS = plans.stream()
                            .map(Mapper::PlanToSummaryUI)
                            .collect(Collectors.toList());

                    plansWithSummary.postValue(planSummaryUIS);
                }

                @Override
                public void onDataError(Exception e) {
                    // Manejar el error según tu lógica, por ejemplo, emitir un error o mostrar un mensaje
                    errorMessage.postValue(e.getMessage());
                }

                @Override
                public void onDataFailed(String error) {
                    // Manejar el error según tu lógica
                    errorMessage.postValue(error);
                }
            });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
