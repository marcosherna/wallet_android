package com.example.wallet.ui.views.home.tabs.all;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.databinding.FragmentTabAllBinding;
import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import java.util.ArrayList;

public class TabAllFragment extends Fragment {

    FragmentTabAllBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TabAllViewModel tabAllViewModel = new ViewModelProvider(this).get(TabAllViewModel.class);
        binding = FragmentTabAllBinding.inflate(inflater, container, false);

        AccountMovementRepository repository = new AccountMovementRepository();

        ArrayList<AccountMovement> lst = repository.getAll();

        RVAccountMovementAdapter adapter = new RVAccountMovementAdapter(lst);
        binding.rvAllAccountMovement.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}