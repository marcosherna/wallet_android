package com.example.wallet.domain.firebase.repository;

import android.util.Log;

import com.example.wallet.domain.models.Plan;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class PlanRepository {

    private static final String TAG = "PlanRepository";
    private final FirebaseFirestore db;
    private final CollectionReference plansCollection;

    public PlanRepository(String userId) {
        this.db = FirebaseFirestore.getInstance();
        this.plansCollection = db.collection("users").document(userId).collection("plans");
    }

    // Obtener todos los planes como modelos de dominio
    public void getAllPlans(OnDataLoadedListener listener) {
        plansCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Plan> plans = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Plan plan = document.toObject(Plan.class);
                        plan.setId(document.getId());
                        plans.add(plan);
                    }
                }
                Log.d(TAG, "Número de planes obtenidos: " + plans.size());
                listener.onDataLoaded(plans);
            } else {
                Log.e(TAG, "Error al obtener planes: ", task.getException());
                listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
            }
        });
    }

    // Guardar un plan nuevo o actualizar uno existente como modelo de dominio
    public void savePlan(Plan plan, OnDataLoadedListener listener) {
        if (plan.getId() == null || plan.getId().isEmpty()) {
            // Nuevo plan
            plansCollection.add(plan).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Plan agregado con éxito.");
                    listener.onDataLoaded(null);
                } else {
                    Log.e(TAG, "Error al agregar plan: ", task.getException());
                    listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
                }
            });
        } else {
            // Actualizar plan existente usando merge
            plansCollection.document(plan.getId()).set(plan, SetOptions.merge()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Plan actualizado con éxito.");
                    listener.onDataLoaded(null);
                } else {
                    Log.e(TAG, "Error al actualizar plan: ", task.getException());
                    listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
                }
            });
        }
    }


    // Eliminar un plan
    public void deletePlan(String planId, OnDataLoadedListener listener) {
        plansCollection.document(planId).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Plan eliminado con éxito.");
                listener.onDataLoaded(null);
            } else {
                Log.e(TAG, "Error al eliminar plan: ", task.getException());
                listener.onDataFailed(task.getException() != null ? task.getException().getMessage() : "Error desconocido");
            }
        });
    }

    // Listener para manejar los datos cargados o errores
    public interface OnDataLoadedListener {
        void onDataLoaded(List<Plan> plans); // Puede ser null para operaciones que no retornan datos
        void onDataError(Exception e);
        void onDataFailed(String error);
    }
}
