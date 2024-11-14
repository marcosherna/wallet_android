package com.example.wallet.ui.views.contactMe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.ContactUI;

import io.reactivex.rxjava3.core.Completable;

public class ContactMeViewModel extends ViewModel {
    public Completable send(ContactUI contactUI){
        return Completable.create(emitter -> {
            try{
                // TODO: implementar
                Thread.sleep(1000);


                String img = contactUI.getImageBase64();

                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }
}