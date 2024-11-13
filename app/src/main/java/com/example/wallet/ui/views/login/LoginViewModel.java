package com.example.wallet.ui.views.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;

public class LoginViewModel extends ViewModel {

    final MutableLiveData<String> email;
    final MutableLiveData<String> password;
    final MutableLiveData<String> message;
    public LiveData<String> getMessage(){ return this.message; }
    final MutableLiveData<Boolean> hasUserSession;
    public LiveData<Boolean> getHasUserSession(){ return this.hasUserSession; }

    public LoginViewModel(){
        this.email = new MutableLiveData<>("");
        this.password = new MutableLiveData<>("");
        this.message = new MutableLiveData<>("");
        this.hasUserSession = new MutableLiveData<>(false);
    }

    public void setEmail(String email){
        this.email.setValue(email);
    }

    public void setPassword(String password){
        this.password.setValue(password);
    }

    public Completable login(){
        return Completable.create(emitter -> {
            try {
                // TODO: implementar login
                Thread.sleep(1000);
                String email = this.email.getValue();
                String password = this.password.getValue();

                if(Objects.equals(email, "admin@gmail.com") &&
                    Objects.equals(password, "admin")){
                    //userSessionUseCase(this.email.getValue(), this.password.getValue());
                    this.hasUserSession.postValue(true);

                } else {
                    this.message.postValue("Incorrecto");
                }
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }
}
