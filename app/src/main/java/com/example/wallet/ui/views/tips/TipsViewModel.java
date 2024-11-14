package com.example.wallet.ui.views.tips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.TipUI;
import com.example.wallet.utils.LogErrorHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TipsViewModel extends ViewModel {
    final CompositeDisposable disposable  = new CompositeDisposable();
    final MutableLiveData<List<TipUI>> tips = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<String> message = new MutableLiveData<>();
    public LiveData<List<TipUI>> getTips() { return this.tips; }
    public LiveData<String> getMessage() { return this.message; }
    public boolean isLoadData = false;
    public void initialize(){
        this.disposable.add(
                this.getAllTips()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this.tips::postValue,
                                throwable -> {
                                    LogErrorHelper.print(throwable);
                                    this.message.postValue("Error al cargar");
                                }
                        )
        );
    }

    private Single<List<TipUI>> getAllTips(){
        return Single.create(emitter -> {
            try{
                Thread.sleep(1000);
                List<String> urlVideos = Arrays.asList(
                        "https://www.youtube.com/watch?v=CFGLoQIhmow",
                        "https://youtu.be/9a2mj2QtRws?list=LL",
                        "https://youtu.be/Gh3oM5fsIqM?list=LL",
                        "https://youtu.be/dYgrDcXHLwA");
                List<TipUI> fakeTips = new ArrayList<>();

                Random random = new Random();

                fakeTips.add(new TipUI(
                        "https://img.youtube.com/vi/CFGLoQIhmow/hqdefault.jpg",
                        "Lorem Ipsum",
                        "simplemente el texto de relleno de las imprentas y archivos de texto ",
                        "https://www.youtube.com/watch?v=CFGLoQIhmow"));

                fakeTips.add(new TipUI(
                        "https://img.youtube.com/vi/9a2mj2QtRws/hqdefault.jpg",
                        "Lorem Ipsum",
                        "Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500 ",
                        "https://youtu.be/9a2mj2QtRws?list=LL"));

                fakeTips.add(new TipUI(
                        "https://img.youtube.com/vi/Gh3oM5fsIqM/hqdefault.jpg",
                        "Lorem Ipsum",
                        "No sólo sobrevivió 500 años, sino que tambien ingresó como texto ",
                        "https://youtu.be/Gh3oM5fsIqM?list=LL"));

                fakeTips.add(new TipUI(
                        "https://img.youtube.com/vi/dYgrDcXHLwA/hqdefault.jpg",
                        "Lorem Ipsum",
                        " T. persona que se dedica a la imprenta) desconocido usó una galería  ",
                        "https://youtu.be/dYgrDcXHLwA"));


                emitter.onSuccess(fakeTips);

            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.disposable.dispose();
    }
}