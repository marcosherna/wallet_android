package com.example.wallet.ui.views.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.UserUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Completable;

public class RegisterViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    final MutableLiveData<Boolean> isCreated;
    final MutableLiveData<String> message;

    public LiveData<Boolean> getIsCreated() { return this.isCreated; }
    public LiveData<String> getMessage() { return this.message; }

    public RegisterViewModel() {
        this.isCreated = new MutableLiveData<>(false);
        this.message = new MutableLiveData<>("");
        this.mAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    public Completable registerUser(UserUI user) {
        return Completable.create(emitter -> {
            try {
                String email = user.getEmail();
                String password = user.getPassword();

                // Registrar el usuario en Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    // Obtenemos el UID del usuario
                                    String uid = firebaseUser.getUid();
                                    saveUserData(uid, user);  // Guardamos datos adicionales en Firestore
                                    this.isCreated.postValue(true);
                                    emitter.onComplete();
                                }
                            } else {
                                String errorMessage = "Error al crear el usuario";
                                if (task.getException() != null) {
                                    errorMessage = task.getException().getMessage();
                                }
                                this.message.postValue(errorMessage);
                                emitter.onError(new Exception(errorMessage));
                            }
                        });
            } catch (Exception e) {
                this.message.postValue("Error: " + e.getMessage());
                emitter.onError(e);
            }
        });
    }

    private void saveUserData(String uid, UserUI user) {
        // Usar directamente UserUI para los datos a guardar
        DocumentReference userRef = firestore.collection("users").document(uid);

        userRef.set(new UserUI(user.getName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getConfirmPassword()))
                .addOnSuccessListener(aVoid -> {
                    // Si la escritura es exitosa, puedes realizar alguna acción aquí
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores si la escritura en Firestore falla
                    this.message.postValue("Error al guardar datos en Firestore");
                });
    }
}
