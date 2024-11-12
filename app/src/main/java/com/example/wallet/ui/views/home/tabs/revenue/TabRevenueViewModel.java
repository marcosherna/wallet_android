package com.example.wallet.ui.views.home.tabs.revenue;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.AccountMovementUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;

public class TabRevenueViewModel extends ViewModel {
    final AccountMovementRepository accountMovementRepository;
    final MutableLiveData<List<AccountMovementUI>> movements;
    public LiveData<List<AccountMovementUI>> getMovements(){ return this.movements; }
    public boolean isLoadData = false;
    public TabRevenueViewModel(){
        this.accountMovementRepository = new AccountMovementRepository();
        this.movements = new MutableLiveData<>(new ArrayList<>());
    }

    public Completable initialize(){
        return Completable.create(emitter -> {
            try {
                Thread.sleep(1000);
                List<AccountMovementUI> movementUIS = this.getMovementsRevenue();
                this.movements.postValue(movementUIS);
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    private List<AccountMovementUI> getMovementsRevenue(){
        return  this.accountMovementRepository.getAllByType(AccountMovement.Type.REVENUE).stream()
                .map(Mapper::AMovementToUI)
                .collect(Collectors.toList());

    }

}