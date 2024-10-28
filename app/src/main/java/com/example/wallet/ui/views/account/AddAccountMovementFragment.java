package com.example.wallet.ui.views.account;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentAddAccountMovementBinding;

public class AddAccountMovementFragment extends Fragment {

    FragmentAddAccountMovementBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            int type = getArguments().getInt("typeMovement");
            String title = getArguments().getString("title");
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AddAccountMovementViewModel addAccountMovementViewModel = new ViewModelProvider(this).get(AddAccountMovementViewModel.class);
        binding = FragmentAddAccountMovementBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }


}