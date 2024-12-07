package com.example.wallet.ui.views.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.core.Completable;

public class LoginViewModel extends ViewModel {

    final MutableLiveData<String> email;
    final MutableLiveData<String> password;
    final MutableLiveData<String> message;
    final MutableLiveData<Boolean> hasUserSession;
    private FirebaseAuth mAuth;

    public LiveData<String> getMessage() {
        return this.message;
    }

    public LiveData<Boolean> getHasUserSession() {
        return this.hasUserSession;
    }

    public LoginViewModel() {
        this.email = new MutableLiveData<>("");
        this.password = new MutableLiveData<>("");
        this.message = new MutableLiveData<>("");
        this.hasUserSession = new MutableLiveData<>(false);
        mAuth = FirebaseAuth.getInstance(); // Inicializamos FirebaseAuth
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public void setUserSession(boolean hasSession) {
        this.hasUserSession.postValue(hasSession);
    }

    // Método para realizar login con FirebaseAuth
    public Completable login() {
        return Completable.create(emitter -> {
            try {
                String email = this.email.getValue();
                String password = this.password.getValue();

                if (email != null && password != null) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        this.hasUserSession.postValue(true); // Si la autenticación es exitosa
                                    }
                                } else {
                                    this.message.postValue("Correo o contraseña incorrectos");
                                    this.hasUserSession.postValue(false);
                                }
                                emitter.onComplete();
                            });
                } else {
                    this.message.postValue("Email o contraseña no proporcionados");
                    emitter.onError(new Throwable("Invalid email or password"));
                }
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}
