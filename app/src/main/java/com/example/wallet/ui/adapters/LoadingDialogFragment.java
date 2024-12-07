package com.example.wallet.ui.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wallet.databinding.DialogLoadingBinding;

public class LoadingDialogFragment extends DialogFragment {

    DialogLoadingBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogLoadingBinding.inflate(inflater, container, false);
        this.setCancelable(false);
        return binding.getRoot();
    }


}
