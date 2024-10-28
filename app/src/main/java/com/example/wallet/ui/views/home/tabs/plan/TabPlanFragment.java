package com.example.wallet.ui.views.home.tabs.plan;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.databinding.FragmentTabPlanBinding;

public class TabPlanFragment extends Fragment {

    FragmentTabPlanBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TabPlanViewModel tabPlanViewModel = new ViewModelProvider(this).get(TabPlanViewModel.class);
        binding = FragmentTabPlanBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}