package com.example.wallet.domain.firebase.repository;

import android.util.Log;

import com.example.wallet.ui.models.AccountMovementUI;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountMovementRepository {

    private static final String TAG = "AccountMovementRepo";
    private final FirebaseFirestore db;
    private final CollectionReference movementsCollection;
    private final String userId;

    public interface OnDataLoadedListener {
        void onDataLoaded(Object data);
        void onDataFailed(String error);
    }

    public AccountMovementRepository(String userId) {
        this.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        this.movementsCollection = db.collection("users").document(userId).collection("account_movements");
    }

    public void getAllMovements(OnDataLoadedListener listener) {
        movementsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<AccountMovementUI> movements = new ArrayList<>();
                if (task.getResult() != null) {
                    for (DocumentSnapshot document : task.getResult()) {
                        AccountMovementUI movement = document.toObject(AccountMovementUI.class);
                        if (movement != null) {
                            movement.setId(document.getId());
                            movements.add(movement);
                        }
                    }
                }
                Log.d(TAG, "Número de movimientos obtenidos: " + movements.size());
                listener.onDataLoaded(movements);
            } else {
                Log.e(TAG, "Error al obtener movimientos: ", task.getException());
                listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
            }
        });
    }

    public void saveMovement(AccountMovementUI movement, OnDataLoadedListener listener) {
        if (movement.getId() == null || movement.getId().isEmpty()) {
            // Nuevo movimiento
            createMovementWithAutoIncrement(movement, listener);
        } else {
            // Actualizar movimiento existente usando update()
            // Preparamos un mapa con los campos a actualizar. Incluye todos los campos del movimiento.
            Map<String, Object> updatedFields = new HashMap<>();
            updatedFields.put("amount", movement.getAmount());
            updatedFields.put("date", movement.getDate());
            updatedFields.put("idPlan", movement.getIdPlan());
            updatedFields.put("typeAccountMovement", movement.getTypeAccountMovement());
            // Si no quieres perder el autoIncrementId, asegúrate de no modificarlo,
            // o inclúyelo también si deseas permitir cambio
            updatedFields.put("autoIncrementId", movement.getAutoIncrementId());
            // Si tu modelo tiene más campos (como isCheck), agrégalos también:
            updatedFields.put("check", movement.isCheck());

            movementsCollection.document(movement.getId()).update(updatedFields)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Movimiento de cuenta actualizado exitosamente");
                            listener.onDataLoaded(null);
                        } else {
                            Log.e(TAG, "Error al actualizar movimiento: ", task.getException());
                            listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
                        }
                    });
        }
    }
    public void updateMovement(AccountMovementUI movement, OnDataLoadedListener listener) {
        // Obtiene el ID del movimiento a actualizar
        String movementId = movement.getId();  // Asegúrate de que 'getId()' esté correctamente implementado en tu modelo

        // Referencia al documento que se va a actualizar
        DocumentReference movementDocRef = movementsCollection.document(movementId);

        // Crea un mapa con los datos que deseas actualizar
        Map<String, Object> updatedMovement = new HashMap<>();
        updatedMovement.put("amount", movement.getAmount());
        updatedMovement.put("date", movement.getDate());
        updatedMovement.put("type", movement.getTypeAccountMovement().toString());  // Ejemplo de cómo actualizar el tipo
        updatedMovement.put("idPlan", movement.getIdPlan());

        // Actualiza el documento en Firestore
        movementDocRef.set(updatedMovement, SetOptions.merge())  // merge asegura que no sobreescriba los demás campos
                .addOnSuccessListener(aVoid -> {
                    // Si se actualiza correctamente
                    Log.d(TAG, "Movimiento actualizado correctamente");
                    listener.onDataLoaded(null);  // O pasar el objeto actualizado si es necesario
                })
                .addOnFailureListener(e -> {
                    // Si ocurre un error
                    Log.e(TAG, "Error al actualizar el movimiento: ", e);
                    listener.onDataFailed(e.getMessage());
                });
    }

    private void createMovementWithAutoIncrement(AccountMovementUI movement, OnDataLoadedListener listener) {
        DocumentReference counterRef = db.collection("users").document(userId)
                .collection("metadata").document("counters");

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(counterRef);
            long currentCounter = 0;
            if (snapshot.exists()) {
                Long storedValue = snapshot.getLong("movementCounter");
                if (storedValue != null) {
                    currentCounter = storedValue;
                }
            }

            long newCounter = currentCounter + 1;

            Map<String, Object> updateData = new HashMap<>();
            updateData.put("movementCounter", newCounter);
            transaction.set(counterRef, updateData, SetOptions.merge());

            movement.setAutoIncrementId(newCounter);
            DocumentReference newMovementRef = movementsCollection.document();
            movement.setId(newMovementRef.getId());
            transaction.set(newMovementRef, movement);
            return null;
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Movimiento de cuenta agregado con autoIncrementId con éxito");
                listener.onDataLoaded(null);
            } else {
                Log.e(TAG, "Error al agregar movimiento con autoIncrementId: ", task.getException());
                listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
            }
        });
    }

    public void deleteMovement(String id, OnDataLoadedListener listener) {
        movementsCollection.document(id).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Movimiento de cuenta eliminado exitosamente");
                listener.onDataLoaded(null);
            } else {
                Log.e(TAG, "Error al eliminar movimiento: ", task.getException());
                listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
            }
        });
    }
}