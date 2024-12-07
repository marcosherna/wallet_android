package com.example.wallet.ui.views.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.R;
import com.example.wallet.ui.adapters.PlanExpandableListAdapter;
import com.example.wallet.ui.models.PlanSummaryUI;
import com.example.wallet.ui.models.PlanUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanFragment extends Fragment implements BSDFormPLan.OnSaveClickListener {

    private PlanViewModel planViewModel;
    private PlanExpandableListAdapter adapter;
    private ExpandableListView expandableListView;
    private View view; // Guardar la vista inflada

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout
        view = inflater.inflate(R.layout.fragment_plan, container, false);

        // Inicializar el Adaptador
        adapter = new PlanExpandableListAdapter(getContext(), true);
        expandableListView = view.findViewById(R.id.expandableListPlan);
        expandableListView.setAdapter(adapter);

        // Inicializar el ViewModel
        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);

        // Configurar el botón para agregar un nuevo plan
        View btnAddPlan = view.findViewById(R.id.btnActionButton);
        btnAddPlan.setOnClickListener(v -> openAddPlanDialog());

        // Observar LiveData de Planes
        planViewModel.getPlansWithSummary().observe(getViewLifecycleOwner(), planSummaries -> {
            // Agrupar los planes por algún criterio, por ejemplo, estado
            List<String> groupTitles = new ArrayList<>();
            HashMap<String, List<PlanSummaryUI>> groupedPlans = new HashMap<>();

            for (PlanSummaryUI summary : planSummaries) {
                String group = summary.getStatus(); // Agrupar por estado, por ejemplo
                if (!groupTitles.contains(group)) {
                    groupTitles.add(group);
                }
                if (!groupedPlans.containsKey(group)) {
                    groupedPlans.put(group, new ArrayList<>());
                }
                groupedPlans.get(group).add(summary);
            }

            adapter.setTitlesPlans(groupTitles);
            adapter.setPlansWithSummary(groupedPlans);
        });

        // Observar LiveData de Errores
        planViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar los listeners para los botones de editar y eliminar en el adaptador
        adapter.setOnListenerPositiveClick(planSummaryUI -> openEditPlanDialog(planSummaryUI));
        adapter.setOnListenerNegativeClick(planSummaryUI -> deletePlan(planSummaryUI));

        // Inicializar la carga de planes
        planViewModel.initialize()
                .subscribe(() -> {
                    // Carga completada
                    Toast.makeText(getContext(), "Planes cargados correctamente.", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    // Manejar error
                    Toast.makeText(getContext(), "Error al cargar planes: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });

        return view;
    }


    private void openAddPlanDialog() {
        BSDFormPLan addPlanDialog = new BSDFormPLan();
        addPlanDialog.setTitle("Agregar Nuevo Plan");
        addPlanDialog.setOnSaveClickListener(this); // Establecer el listener
        addPlanDialog.show(getParentFragmentManager(), "BSDFormPLan");
    }


    private void openEditPlanDialog(PlanSummaryUI planSummaryUI) {
        BSDFormPLan editPlanDialog = new BSDFormPLan();
        editPlanDialog.setTitle("Editar Plan");
        // Obtener el PlanUI correspondiente
        PlanUI planUI = findPlanUIById(planSummaryUI.getIdPlan());
        editPlanDialog.setPlanSelected(planUI);
        editPlanDialog.setOnSaveClickListener(this); // Establecer el listener
        editPlanDialog.show(getParentFragmentManager(), "BSDFormPLan");
    }


    private PlanUI findPlanUIById(String planId) {
        List<PlanSummaryUI> plans = planViewModel.getPlansWithSummary().getValue();
        if (plans != null) {
            for (PlanSummaryUI summary : plans) {
                if (summary.getIdPlan().equals(planId)) {
                    // Aquí puedes convertir PlanSummaryUI a PlanUI si es necesario
                    // Asumiendo que tienes los datos necesarios
                    return new PlanUI(
                            summary.getIdPlan(),
                            summary.getNamePlan(),
                            summary.getDescription(),
                            summary.getTerm(),
                            summary.getTotal() // O cualquier otro campo relevante
                    );
                }
            }
        }
        return null;
    }


    private void deletePlan(PlanSummaryUI planSummaryUI) {
        planViewModel.deletePlan(planSummaryUI)
                .subscribe(() -> {
                    Toast.makeText(getContext(), "Plan eliminado correctamente.", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    Toast.makeText(getContext(), "Error al eliminar plan: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onSaveClicked(PlanUI planUI) {
        if (planUI.getId() == null || planUI.getId().isEmpty()) {
            // Es un nuevo plan
            planViewModel.addPlan(planUI)
                    .subscribe(() -> {
                        Toast.makeText(getContext(), "Plan agregado correctamente.", Toast.LENGTH_SHORT).show();
                    }, throwable -> {
                        Toast.makeText(getContext(), "Error al agregar plan: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Es una edición de un plan existente
            planViewModel.editPlan(planUI)
                    .subscribe(() -> {
                        Toast.makeText(getContext(), "Plan editado correctamente.", Toast.LENGTH_SHORT).show();
                    }, throwable -> {
                        Toast.makeText(getContext(), "Error al editar plan: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpieza si es necesario
    }
}
