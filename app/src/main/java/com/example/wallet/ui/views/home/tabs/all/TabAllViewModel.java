package com.example.wallet.ui.views.home.tabs.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.AccountMovementUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;

public class TabAllViewModel extends ViewModel {
    final AccountMovementRepository accountMovementRepository;
    final MutableLiveData<List<AccountMovementUI>> movements;
    public LiveData<List<AccountMovementUI>> getMovements(){ return this.movements; }
    public TabAllViewModel(){
        this.accountMovementRepository = new AccountMovementRepository();
        this.movements = new MutableLiveData<>(new ArrayList<>());
    }

    public Completable initialize(){
        return  Completable.create(emitter -> {
            try {
                Thread.sleep(1000); // simulamos un tiempo de espera
                this.movements.postValue(this.getMovementsUI());
                emitter.onComplete();
            }catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    private List<AccountMovementUI> getMovementsUI() {
        // TODO: implementar el obtener los datos
        return this.accountMovementRepository.getAll().stream()
                .map(Mapper::AMovementToUI)
                .collect(Collectors.toList());
    }


}