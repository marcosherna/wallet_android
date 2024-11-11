package com.example.wallet.ui.views.home.tabs.all;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentTabAllBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabAllFragment extends Fragment {

    FragmentTabAllBinding binding;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    TabAllViewModel tabAllViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tabAllViewModel = new ViewModelProvider(this).get(TabAllViewModel.class);
        binding = FragmentTabAllBinding.inflate(inflater, container, false);

        RVAccountMovementAdapter adapter = new RVAccountMovementAdapter();
        binding.rvAllAccountMovement.setAdapter(adapter);

        tabAllViewModel.getMovements().observe(getViewLifecycleOwner(), adapter::setItems);

        binding.swipeRefreshLayout.setOnRefreshListener(this::loadData);

        loadData();


        return binding.getRoot();
    }

    private void loadData(){
        binding.swipeRefreshLayout.setRefreshing(true);
        compositeDisposable.add(
                tabAllViewModel.initialize()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> binding.swipeRefreshLayout.setRefreshing(false), // Finaliza el indicador al completar
                                throwable -> {
                                    binding.swipeRefreshLayout.setRefreshing(false);
                                    Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.clear();
    }
}