package com.example.wallet.ui.views.tips;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.R;
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

        RVTipAdapter tipAdapter = new RVTipAdapter();
        this.binding.rvTips.setAdapter(tipAdapter);

        tipAdapter.setOnListenerTipClick( tipUI -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tipUI.getYoutubeUrl()));
            startActivity(intent);
        });

        if(this.tipsViewModel != null && !this.tipsViewModel.isLoadData){
            this.tipsViewModel.initialize();
        }

        if(this.tipsViewModel != null){
            this.tipsViewModel.message.observe(getViewLifecycleOwner(), message ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());

            this.tipsViewModel.getTips().observe(getViewLifecycleOwner(), tipAdapter::setTipsList);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}