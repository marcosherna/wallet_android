package com.example.wallet.ui.views.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.ui.models.UserUI;
import com.example.wallet.utils.EncryptionUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel {
    final MutableLiveData<UserUI> userSession;

    public ProfileViewModel() {
        this.userSession = new MutableLiveData<>(new UserUI());
    }

    public LiveData<UserUI> getUserSession() {
        return this.userSession;
    }

    // Método para obtener los datos del usuario desde Firestore utilizando el UID
    public Completable findUserSession() {
        return Completable.create(emitter -> {
            try {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .document(uid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String name = task.getResult().getString("name");
                                String lastName = task.getResult().getString("lastName");
                                String email = task.getResult().getString("email");

                                UserUI user = new UserUI(name, lastName, email, "", "");
                                this.userSession.postValue(user);
                                emitter.onComplete();
                            } else {
                                emitter.onError(new Exception("Error al obtener datos del usuario"));
                            }
                        })
                        .addOnFailureListener(emitter::onError);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    // Método para actualizar los datos del usuario en Firestore y Firebase Auth
    public Completable updateUser(UserUI userUI) {
        return Completable.create(emitter -> {
            try {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Encriptamos la contraseña antes de actualizarla
                String encryptedPassword = EncryptionUtil.encryptPassword(userUI.getPassword());

                // 1. Actualizamos Firestore con los datos del usuario
                db.collection("users")
                        .document(uid)
                        .update(
                                "name", userUI.getName(),
                                "lastName", userUI.getLastName(),
                                "email", userUI.getEmail(),
                                "password", encryptedPassword // Enviamos la contraseña encriptada
                        )
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // 2. Actualizamos Firebase Auth (email y password)
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser currentUser = auth.getCurrentUser();

                                if (currentUser != null) {
                                    // Cambiar email en Firebase Auth
                                    currentUser.updateEmail(userUI.getEmail())
                                            .addOnCompleteListener(emailTask -> {
                                                if (emailTask.isSuccessful()) {
                                                    // Cambiar la contraseña en Firebase Auth
                                                    currentUser.updatePassword(userUI.getPassword())
                                                            .addOnCompleteListener(passwordTask -> {
                                                                if (passwordTask.isSuccessful()) {
                                                                    // Actualizamos el perfil del usuario en Firebase Auth (nombre, apellido)
                                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                            .setDisplayName(userUI.getName() + " " + userUI.getLastName())
                                                                            .build();

                                                                    currentUser.updateProfile(profileUpdates)
                                                                            .addOnCompleteListener(profileTask -> {
                                                                                if (profileTask.isSuccessful()) {
                                                                                    // Si todo es exitoso, actualizamos el LiveData y completamos
                                                                                    this.userSession.postValue(userUI);
                                                                                    emitter.onComplete();
                                                                                } else {
                                                                                    emitter.onError(new Exception("Error al actualizar el perfil"));
                                                                                }
                                                                            });
                                                                } else {
                                                                    emitter.onError(new Exception("Error al actualizar la contraseña"));
                                                                }
                                                            });
                                                } else {
                                                    emitter.onError(new Exception("Error al actualizar el email"));
                                                }
                                            });
                                } else {
                                    emitter.onError(new Exception("Usuario no autenticado"));
                                }
                            } else {
                                emitter.onError(new Exception("Error al actualizar datos en Firestore"));
                            }
                        })
                        .addOnFailureListener(emitter::onError);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    // Método para cerrar sesión
    public Completable logOut() {
        return Completable.create(emitter -> {
            try {
                FirebaseAuth.getInstance().signOut();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }
}
