package com.example.wallet.ui.views.home.tabs.all;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentTabAllBinding;

public class TabAllFragment extends Fragment {

    FragmentTabAllBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TabAllViewModel tabAllViewModel = new ViewModelProvider(this).get(TabAllViewModel.class);
        binding = FragmentTabAllBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}