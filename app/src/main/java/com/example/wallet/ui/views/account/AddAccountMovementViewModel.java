package com.example.wallet.ui.views.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.mappers.Mapper;
import com.example.wallet.ui.models.AccountMovementUI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class  AddAccountMovementViewModel extends ViewModel {
    final AccountMovementRepository repository;
    final MutableLiveData<AccountMovement.Type> typeAccount;
    final MutableLiveData<List<AccountMovementUI>> movements;

    public AddAccountMovementViewModel(){
        this.repository = new AccountMovementRepository();
        this.movements = new MutableLiveData<>();
        this.typeAccount = new MutableLiveData<>();
        initialize();
    }

    private void initialize(){
        if(this.typeAccount != null){
            List<AccountMovementUI> _movements;
            _movements = this.repository.getAllByType(this.typeAccount.getValue())
                    .stream().map(Mapper::AMovementToUI).collect(Collectors.toList());

            this.movements.setValue(_movements);
        }
    }

    public LiveData<List<AccountMovementUI>> getMovements() { return this.movements; }
    public LiveData<AccountMovement.Type> getTypeAccountMovement() { return this.typeAccount; }
    public void setTypeAccount(AccountMovement.Type type){
        this.typeAccount.setValue(type);
        this.initialize();
    }




}