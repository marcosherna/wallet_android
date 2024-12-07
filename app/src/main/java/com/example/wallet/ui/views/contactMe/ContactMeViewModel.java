package com.example.wallet.ui.views.contactMe;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Completable;

public class ContactMeViewModel extends ViewModel {
    public Completable send(){
        return Completable.create(emitter -> {
            try{
                // TODO: implementar
                Thread.sleep(1000);



                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }
}