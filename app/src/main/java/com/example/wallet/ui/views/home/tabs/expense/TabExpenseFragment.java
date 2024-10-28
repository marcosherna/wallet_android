package com.example.wallet.ui.views.home.tabs.expense;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentTabExpenseBinding;

public class TabExpenseFragment extends Fragment {

    FragmentTabExpenseBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TabExpenseViewModel tabExpenseViewModel = new ViewModelProvider(this).get(TabExpenseViewModel.class);
        binding = FragmentTabExpenseBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}