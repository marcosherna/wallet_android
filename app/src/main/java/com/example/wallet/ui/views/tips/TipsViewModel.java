package com.example.wallet.ui.views.tips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.TipUI;
import com.example.wallet.utils.LogErrorHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TipsViewModel extends ViewModel {
    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<List<TipUI>> tips = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<String> message = new MutableLiveData<>();
    public LiveData<List<TipUI>> getTips() { return this.tips; }
    public LiveData<String> getMessage() { return this.message; }
    public boolean isLoadData = false;

    public void initialize() {
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

    private Single<List<TipUI>> getAllTips() {
        return Single.create(emitter -> {
            try {
                Thread.sleep(1000); // Simula un retraso

                // Lista de videos de educación financiera de YouTube con miniaturas
                List<TipUI> videoList = new ArrayList<>();
                videoList.add(new TipUI(
                        "https://i.ytimg.com/vi/9sCVcWD1Svs/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLBo0qaBS4VhnWRJ39CZxgHXI5FjEg", // Thumbnail
                        "Introducción a la educación financiera",
                        "Educación Financiera para Principiantes.",
                        "https://www.youtube.com/watch?v=9sCVcWD1Svs&ab_channel=BetterWalletenEspa%C3%B1ol"
                ));
                videoList.add(new TipUI(
                        "https://i.ytimg.com/vi/X38MGyuc0ds/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLAqNzMVumVMRtIS8yyaTALrmfUM4g", // Thumbnail
                        "Cómo hacer un presupuesto personal",
                        "¿Como Funcionan las finanzas?",
                        "https://www.youtube.com/watch?v=X38MGyuc0ds&ab_channel=AprendizFinanciero"
                ));
                videoList.add(new TipUI(
                        "https://i.ytimg.com/vi/VFfdt2xHDxU/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLBlXLl07zV8-eZ6iYQyb6ud6Tr9NA", // Thumbnail
                        "Invertir en la bolsa de valores para principiantes",
                        "Consejos y estrategias para comenzar a invertir en la bolsa.",
                        "https://www.youtube.com/watch?v=VFfdt2xHDxU&ab_channel=LaBolsaparaprincipiantes"
                ));
                videoList.add(new TipUI(
                        "https://i.ytimg.com/vi/IzQgef5FA5Y/hqdefault.jpg?sqp=-oaymwEnCOADEI4CSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLAI9dd_ODmYfSxpC-Ot9tARULZjuw", // Thumbnail
                        "La importancia del ahorro a largo plazo",
                        "Aprende por qué es fundamental ahorrar para tu futuro.",
                        "https://www.youtube.com/watch?v=IzQgef5FA5Y&ab_channel=SBSPer%C3%BA"
                ));
                videoList.add(new TipUI(
                        "https://i.ytimg.com/vi/qQe6ClgRuio/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLDkmbBTPUBU9xN5CgBzGqzzQHXKfg", // Thumbnail
                        "Cómo invertir en bienes raíces",
                        "Descubre cómo comenzar a invertir en el mercado inmobiliario.",
                        "https://www.youtube.com/watch?v=qQe6ClgRuio&ab_channel=EduardoRosas-FinanzasPersonales"
                ));
                videoList.add(new TipUI(
                        "https://i.ytimg.com/vi/Ow376vKIBXE/hq720.jpg?sqp=-oaymwEnCNAFEJQDSFryq4qpAxkIARUAAIhCGAHYAQHiAQoIGBACGAY4AUAB&rs=AOn4CLBij1Kjha2HMvLXe6RPff0_sVqHIA", // Thumbnail
                        "Criptomonedas: lo que debes saber",
                        "Un video introductorio sobre qué son las criptomonedas y cómo invertir en ellas.",
                        "https://www.youtube.com/watch?v=Ow376vKIBXE&ab_channel=Teloexplicovideo"
                ));


                emitter.onSuccess(videoList);
            } catch (InterruptedException e) {
                emitter.onError(e);
            }
        });
    }
}
