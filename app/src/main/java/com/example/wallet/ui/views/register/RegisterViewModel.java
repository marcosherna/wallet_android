package com.example.wallet.ui.views.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.UserUI;

import io.reactivex.rxjava3.core.Completable;

public class RegisterViewModel extends ViewModel {
    final MutableLiveData<Boolean> isCreated;
    public LiveData<Boolean> getIsCreated() { return this.isCreated; }
    public RegisterViewModel(){
        this.isCreated = new MutableLiveData<>(false);
    }

    public Completable registerUser(UserUI user){
        return Completable.create(emitter -> {
            try{
                // TODO: implementar metodo
                Thread.sleep(1000);
                this.isCreated.postValue(true);
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }

        });
    }
}