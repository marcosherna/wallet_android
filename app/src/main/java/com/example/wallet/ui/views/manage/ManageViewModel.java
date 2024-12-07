package com.example.wallet.ui.views.manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.firebase.repository.AccountMovementRepository;
import com.example.wallet.domain.firebase.repository.PlanRepository;
import com.example.wallet.domain.models.Plan;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.Completable;

public class ManageViewModel extends ViewModel {
    final PlanRepository planRepository;
    final AccountMovementRepository accountMovementRepository;
    final List<AccountMovementUI> movementUISCache;

    final MutableLiveData<List<PlanUI>> plans;
    public LiveData<List<PlanUI>> getPlans() { return this.plans; }

    final MutableLiveData<List<AccountMovementUI>> movements;
    public LiveData<List<AccountMovementUI>> getMovements() { return this.movements; }

    public boolean isLoadData = false;

    public ManageViewModel(){
        // Asegúrate que el usuario esté autenticado antes
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.planRepository = new PlanRepository(userId);
        this.accountMovementRepository = new AccountMovementRepository(userId);
        this.plans = new MutableLiveData<>(new ArrayList<>());
        this.movements = new MutableLiveData<>(new ArrayList<>());
        this.movementUISCache = new ArrayList<>();
    }

    public List<AccountMovementUI> getMovementUISCache() { return this.movementUISCache; }

    public Completable initialize(){
        return Completable.create(emitter -> {
            initializePlan(emitter, true);
        });
    }

    private void initializePlan(io.reactivex.rxjava3.core.CompletableEmitter outerEmitter, boolean loadMovementsAfter) {
        planRepository.getAllPlans(new PlanRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Plan> domainPlans) {
                List<PlanUI> planUIS = new ArrayList<>();
                for (Plan p : domainPlans) {
                    planUIS.add(Mapper.PlanToUI(p));
                }
                plans.postValue(planUIS);

                if (loadMovementsAfter) {
                    initializeMovement(outerEmitter);
                }
            }

            @Override
            public void onDataError(Exception e) {
                if (outerEmitter != null && !outerEmitter.isDisposed()) {
                    outerEmitter.onError(e);
                }
            }

            @Override
            public void onDataFailed(String error) {
                if (outerEmitter != null && !outerEmitter.isDisposed()) {
                    outerEmitter.onError(new Exception(error));
                }
            }
        });
    }

    private void initializeMovement(io.reactivex.rxjava3.core.CompletableEmitter outerEmitter) {
        accountMovementRepository.getAllMovements(new AccountMovementRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(Object data) {
                @SuppressWarnings("unchecked")
                List<AccountMovementUI> movementList = (List<AccountMovementUI>) data;

                movementUISCache.clear();
                movementUISCache.addAll(movementList);
                movements.postValue(movementList);

                if (outerEmitter != null && !outerEmitter.isDisposed()) {
                    outerEmitter.onComplete();
                }
            }

            @Override
            public void onDataFailed(String error) {
                if (outerEmitter != null && !outerEmitter.isDisposed()) {
                    outerEmitter.onError(new Exception(error));
                }
            }
        });
    }

    public Completable updateMovement(AccountMovementUI movementUI){
        return Completable.create(emitter -> {
            accountMovementRepository.saveMovement(movementUI, new AccountMovementRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(Object data) {
                    refreshMovements(emitter);
                }

                @Override
                public void onDataFailed(String error) {
                    emitter.onError(new Exception(error));
                }
            });
        });
    }

    public Completable addMovements(AccountMovementUI movementUI){
        return Completable.create(emitter -> {
            accountMovementRepository.saveMovement(movementUI, new AccountMovementRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(Object data) {
                    refreshMovements(emitter);
                }

                @Override
                public void onDataFailed(String error) {
                    emitter.onError(new Exception(error));
                }
            });
        });
    }

    private void refreshMovements(io.reactivex.rxjava3.core.CompletableEmitter emitter){
        accountMovementRepository.getAllMovements(new AccountMovementRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(Object data) {
                @SuppressWarnings("unchecked")
                List<AccountMovementUI> movementList = (List<AccountMovementUI>) data;

                movementUISCache.clear();
                movementUISCache.addAll(movementList);
                movements.postValue(movementList);

                emitter.onComplete();
            }

            @Override
            public void onDataFailed(String error) {
                emitter.onError(new Exception(error));
            }
        });
    }

    public Completable deletingMovements(List<AccountMovementUI> selections){
        return Completable.create(emitter -> {
            if (selections.isEmpty()) {
                emitter.onComplete();
                return;
            }
            deleteMovementsRecursively(selections, 0, emitter);
        });
    }

    private void deleteMovementsRecursively(List<AccountMovementUI> movementsToDelete, int index, io.reactivex.rxjava3.core.CompletableEmitter emitter) {
        if (index >= movementsToDelete.size()) {
            refreshMovements(emitter);
            return;
        }

        AccountMovementUI movement = movementsToDelete.get(index);
        accountMovementRepository.deleteMovement(movement.getId(), new AccountMovementRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(Object data) {
                deleteMovementsRecursively(movementsToDelete, index + 1, emitter);
            }

            @Override
            public void onDataFailed(String error) {
                emitter.onError(new Exception(error));
            }
        });
    }

    public void filterByType(TypeAccountMovement typeAccountMovement){
        List<AccountMovementUI> filter = new ArrayList<>();
        for (AccountMovementUI m : movementUISCache) {
            if (m.getTypeAccountMovement() == typeAccountMovement) {
                filter.add(m);
            }
        }
        movements.setValue(filter);
    }

    public void getAllRecent(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        List<AccountMovementUI> sortedList = new ArrayList<>(movementUISCache);
        sortedList.sort((m1, m2) -> {
            try {
                Date date1 = sdf.parse(m1.getDate());
                Date date2 = sdf.parse(m2.getDate());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                return 0;
            }
        });
        movements.postValue(sortedList);
    }
}