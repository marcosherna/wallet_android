package com.example.wallet.ui.views.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.UserUI;

import io.reactivex.rxjava3.core.Completable;

public class ProfileViewModel extends ViewModel {
    final MutableLiveData<UserUI> userSession;
    public LiveData<UserUI> getUserSession() { return this.userSession; }
    public ProfileViewModel(){
        this.userSession = new MutableLiveData<>(new UserUI());
    }

    public Completable findUserSession(){
        return Completable.create(emitter -> {
            try {
                // TODO: implementar
                Thread.sleep(1000);
                // update property userSession
                UserUI fakeUser = new UserUI();

                fakeUser.setName("Juan");
                fakeUser.setLastName("Perez");
                fakeUser.setEmail("john.mclean@examplepetstore.com");
                fakeUser.setPassword("admin");

                this.userSession.postValue(fakeUser);

                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public Completable updateUser(UserUI userUI){
        return Completable.create(emitter -> {
            try {
                // TODO: implementar
                Thread.sleep(1000);
                // update property userSession
                this.userSession.postValue(userUI);

                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    public Completable logOut(){
        return Completable.create(emitter -> {
            try {
                Thread.sleep(1000);
                // borra el usuario en sesion
                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }




}