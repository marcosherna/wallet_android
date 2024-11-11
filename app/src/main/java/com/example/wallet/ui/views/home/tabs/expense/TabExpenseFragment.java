package com.example.wallet.ui.views.home.tabs.expense;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.databinding.FragmentTabExpenseBinding;
import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import java.util.ArrayList;

public class TabExpenseFragment extends Fragment {

    FragmentTabExpenseBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TabExpenseViewModel tabExpenseViewModel = new ViewModelProvider(this).get(TabExpenseViewModel.class);
        binding = FragmentTabExpenseBinding.inflate(inflater, container, false);

        AccountMovementRepository repository = new AccountMovementRepository();
        ArrayList<AccountMovement> lst = repository.getAllByType(AccountMovement.Type.EXPENSE);

        binding.rvAllAccountExpense.setAdapter(new RVAccountMovementAdapter());


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}