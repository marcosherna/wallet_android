package com.example.wallet.ui.views.manage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.fake.repository.PlanRepository;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.PlanUI;
import com.example.wallet.ui.models.TypeAccountMovement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;

public class ManageViewModel extends ViewModel {
    final PlanRepository planRepository;
    final AccountMovementRepository accountMovementRepository;
    final List<AccountMovementUI> movementUISCache;
    public List<AccountMovementUI> getMovementUISCache() { return this.movementUISCache; }
    final MutableLiveData<List<PlanUI>> plans;
    public LiveData<List<PlanUI>> getPlans() { return this.plans; }
    final MutableLiveData<List<AccountMovementUI>> movements;
    public LiveData<List<AccountMovementUI>> getMovements() { return this.movements; }
    public boolean isLoadData = false;
    public ManageViewModel(){
        this.planRepository = new PlanRepository();
        this.plans = new MutableLiveData<>(new ArrayList<>());
        this.accountMovementRepository = new AccountMovementRepository();
        this.movements = new MutableLiveData<>(new ArrayList<>());
        this.movementUISCache = new ArrayList<>();
    }

    public Completable initialize(){
        return Completable.create(emitter -> {
            try{
                // TODO: Cargar los datos necesarios para la vista
                Thread.sleep(1000);
                this.initializeMovement();
                this.initializePlan();

                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }
    public Completable updateMovement(AccountMovementUI movementUI){
        return Completable.create( emitter -> {
            try {
                Thread.sleep(1000);

                this.initializeMovement();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }
    private void initializePlan(){
        // TODO: Reemplazar con el origen de datos online
        List<PlanUI> plans = this.planRepository.getAll().stream()
                .map(Mapper::PlanToUI)
                .collect(Collectors.toList());
        this.plans.postValue(plans);
    }

    private void initializeMovement() {
        // TODO: Reemplazar con el origen de datos online
        List<AccountMovementUI> movementUIS = this.accountMovementRepository.getAll().stream()
                .map(Mapper::AMovementToUI).collect(Collectors.toList());

        // Lo guardamos en cache para hacer los filtros
        this.movementUISCache.clear();
        this.movementUISCache.addAll(movementUIS);

        this.movements.postValue(movementUIS);
    }

    public Completable deletingMovements(List<AccountMovementUI> selections){
        return Completable.create(emitter -> {
            try {
                // TODO: Elimiar todos los planes seleccionados
                Thread.sleep(1000);

                this.initializeMovement();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public Completable addMovements(AccountMovementUI movementUI){
        return Completable.create(emitter -> {
            try {
                // TODO: agregar el movimento
                Thread.sleep(1000);

                this.initializeMovement();
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public void filterByType(TypeAccountMovement typeAccountMovement){
        if(this.movements.getValue() != null){
            List<AccountMovementUI> filter = this.movementUISCache.stream()
                    .filter(m -> m.getTypeAccountMovement() == typeAccountMovement)
                    .collect(Collectors.toList());

            this.movements.setValue(filter);
        }
    }

    public void getAllRecent(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        List<AccountMovementUI> sortedList = this.movementUISCache.stream()
                .sorted((m1, m2) -> {
                    try {
                        Date date1 = sdf.parse(m1.getDate());
                        Date date2 = sdf.parse(m2.getDate());
                        return date2.compareTo(date1);
                    } catch (ParseException e) {
                        //e.printStackTrace();
                        return 0;
                    }
                })
                .collect(Collectors.toList());

        this.movements.postValue(sortedList);
    }




}