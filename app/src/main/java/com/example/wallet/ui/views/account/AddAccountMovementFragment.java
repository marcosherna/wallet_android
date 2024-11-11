package com.example.wallet.ui.views.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.renderscript.RenderScript;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentAddAccountMovementBinding;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.adapters.RVAccountMovementWithCheck;
import com.example.wallet.ui.models.AccountMovementUI;

import java.util.List;
import java.util.Objects;

public class AddAccountMovementFragment extends Fragment {

    FragmentAddAccountMovementBinding binding;
    AccountMovement.Type typeAccountMovement;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null){
            //this.typeAccountMovement = (AccountMovement.Type)getArguments().getSerializable("typeMovement");
            String title = getArguments().getString("title");
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(title);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AddAccountMovementViewModel addAccountMovementViewModel = new ViewModelProvider(this).get(AddAccountMovementViewModel.class);
        binding = FragmentAddAccountMovementBinding.inflate(inflater, container, false);

        RVAccountMovementWithCheck rvAdapter = new RVAccountMovementWithCheck();
        binding.rvAddAccountMovement.setAdapter(rvAdapter);

        assert getArguments() != null;
        this.typeAccountMovement = (AccountMovement.Type)getArguments()
                .getSerializable("typeMovement");

        addAccountMovementViewModel.setTypeAccount(this.typeAccountMovement);

//        addAccountMovementViewModel.getMovements()
//                .observe(getViewLifecycleOwner(), movements ->
//                        movements.forEach(rvAdapter::addMovement));


        binding.fabDeleteAccountMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AccountMovementUI> lst = rvAdapter.getItems();
                lst.forEach( m -> {

                    if(m.isCheck()){
                        Toast.makeText(getContext(), "Hay elementos seleccionados", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return binding.getRoot();
    }


}