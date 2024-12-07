package com.example.wallet.ui.views.home.tabs.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.databinding.FragmentTabAllBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabAllFragment extends Fragment {
    private FragmentTabAllBinding binding;
    private TabAllViewModel viewModel;
    private RVAccountMovementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabAllBinding.inflate(inflater, container, false);

        // Inicializar el adapter con una lista vacía
        adapter = new RVAccountMovementAdapter();
        binding.rvAllAccountMovement.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TabAllViewModel.class);

        // Llamar initialize para cargar datos desde Firebase a través del ViewModel
        viewModel.initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> { /* Éxito, se cargaron datos en LiveData */ },
                        throwable -> { /* Error al cargar datos */ }
                );

        // Observar los cambios en movimientos
        viewModel.getMovements().observe(getViewLifecycleOwner(), movements -> {
            // Cuando LiveData notifique cambios (por ejemplo al terminar initialize), actualizamos el adapter
            adapter.setItems(movements);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
