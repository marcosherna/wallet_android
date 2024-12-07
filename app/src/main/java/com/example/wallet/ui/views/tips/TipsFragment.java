package com.example.wallet.ui.views.tips;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.databinding.FragmentTipsBinding;
import com.example.wallet.ui.adapters.RVTipAdapter;

public class TipsFragment extends Fragment {

    FragmentTipsBinding binding;
    TipsViewModel tipsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.tipsViewModel = new ViewModelProvider(this).get(TipsViewModel.class);
        this.binding = FragmentTipsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar el Adapter del RecyclerView
        RVTipAdapter tipAdapter = new RVTipAdapter();
        this.binding.rvTips.setAdapter(tipAdapter);

        // Configurar el listener del clic
        tipAdapter.setOnListenerTipClick(tipUI -> {
            // Crear el Intent para abrir el video en YouTube
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tipUI.getYoutubeUrl()));
            startActivity(intent); // Iniciar la actividad de YouTube
        });

        // Verificar si los datos ya se han cargado
        if (this.tipsViewModel != null && !this.tipsViewModel.isLoadData) {
            this.tipsViewModel.initialize(); // Inicializar los datos (cargar los tips)
        }

        // Observar los mensajes y actualizar la lista de tips
        if (this.tipsViewModel != null) {
            this.tipsViewModel.message.observe(getViewLifecycleOwner(), message ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());

            // Observa los datos de tips y actualiza el RecyclerView
            this.tipsViewModel.getTips().observe(getViewLifecycleOwner(), tipAdapter::setTipsList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar referencias, si es necesario
    }
}
