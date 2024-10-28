package com.example.wallet.ui.views.home.tabs.revenue;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.databinding.FragmentTabRevenueBinding;

public class TabRevenueFragment extends Fragment {
    FragmentTabRevenueBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TabRevenueViewModel tabRevenueViewModel = new ViewModelProvider(this).get(TabRevenueViewModel.class);
        binding = FragmentTabRevenueBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}