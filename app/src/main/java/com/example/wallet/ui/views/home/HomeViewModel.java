package com.example.wallet.ui.views.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.firebase.repository.AccountMovementRepository;
import com.example.wallet.domain.firebase.repository.PlanRepository;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<AccountMovementUI>> movementsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Plan>> plansLiveData = new MutableLiveData<>(new ArrayList<>());

    private PlanRepository planRepository;
    private AccountMovementRepository movementRepository;

    public HomeViewModel() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Usuario no autenticado. Podrías:
            // 1. Lanzar una excepción controlada:
            // throw new IllegalStateException("Usuario no autenticado");

            // 2. O simplemente retornar y dejar las listas vacías, y que el fragment maneje el caso:
            return;
        }

        String userId = currentUser.getUid();
        planRepository = new PlanRepository(userId);
        movementRepository = new AccountMovementRepository(userId);

        loadAllData();
    }

    public LiveData<List<AccountMovementUI>> getMovementsLiveData(){
        return movementsLiveData;
    }

    public LiveData<List<Plan>> getPlansLiveData(){
        return plansLiveData;
    }

    private void loadAllData(){
        if (planRepository != null) {
            planRepository.getAllPlans(new PlanRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(List<Plan> plans) {
                    plansLiveData.postValue(plans);
                }

                @Override
                public void onDataError(Exception e) {}

                @Override
                public void onDataFailed(String error) {}
            });
        }

        if (movementRepository != null) {
            movementRepository.getAllMovements(new AccountMovementRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(Object data) {
                    @SuppressWarnings("unchecked")
                    List<AccountMovementUI> movements = (List<AccountMovementUI>) data;
                    movementsLiveData.postValue(movements);
                }

                @Override
                public void onDataFailed(String error) {}
            });
        }
    }

    public List<AccountMovementUI> getAllMovements(){
        return movementsLiveData.getValue() != null ? movementsLiveData.getValue() : new ArrayList<>();
    }

    public List<AccountMovementUI> getExpenses(){
        List<AccountMovementUI> all = getAllMovements();
        List<AccountMovementUI> expenses = new ArrayList<>();
        for (AccountMovementUI m : all){
            if (m.getTypeAccountMovement() == TypeAccountMovement.EXPENSE) expenses.add(m);
        }
        return expenses;
    }

    public List<AccountMovementUI> getRevenues(){
        List<AccountMovementUI> all = getAllMovements();
        List<AccountMovementUI> revenues = new ArrayList<>();
        for (AccountMovementUI m : all){
            if (m.getTypeAccountMovement() == TypeAccountMovement.REVENUE) revenues.add(m);
        }
        return revenues;
    }

    public List<Plan> getAllPlans(){
        return plansLiveData.getValue() != null ? plansLiveData.getValue() : new ArrayList<>();
    }
}
