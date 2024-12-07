package com.example.wallet.ui.views.home.tabs.revenue;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.domain.firebase.repository.AccountMovementRepository;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.TypeAccountMovement;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;

public class TabRevenueViewModel extends ViewModel {
    final AccountMovementRepository accountMovementRepository;
    final MutableLiveData<List<AccountMovementUI>> movements;
    public LiveData<List<AccountMovementUI>> getMovements(){ return this.movements; }
    public boolean isLoadData = false;

    public TabRevenueViewModel(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.accountMovementRepository = new AccountMovementRepository(userId);
        this.movements = new MutableLiveData<>(new ArrayList<>());
    }

    public Completable initialize(){
        return Completable.create(emitter -> {
            accountMovementRepository.getAllMovements(new AccountMovementRepository.OnDataLoadedListener() {
                @Override
                public void onDataLoaded(Object data) {
                    @SuppressWarnings("unchecked")
                    List<AccountMovementUI> allMovements = (List<AccountMovementUI>) data;
                    // Filtrar solo ingresos
                    List<AccountMovementUI> revenues = new ArrayList<>();
                    for (AccountMovementUI m : allMovements) {
                        if (m.getTypeAccountMovement() == TypeAccountMovement.REVENUE) {
                            revenues.add(m);
                        }
                    }
                    movements.postValue(revenues);
                    emitter.onComplete();
                }

                @Override
                public void onDataFailed(String error) {
                    emitter.onError(new Exception(error));
                }
            });
        });
    }
}
