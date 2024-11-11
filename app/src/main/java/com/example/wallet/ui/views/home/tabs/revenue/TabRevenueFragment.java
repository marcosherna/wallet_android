package com.example.wallet.ui.views.home.tabs.revenue;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentTabRevenueBinding;
import com.example.wallet.domain.fake.repository.AccountMovementRepository;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabRevenueFragment extends Fragment {
    FragmentTabRevenueBinding binding;
    TabRevenueViewModel tabRevenueViewModel;
    final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tabRevenueViewModel = new ViewModelProvider(this).get(TabRevenueViewModel.class);
        binding = FragmentTabRevenueBinding.inflate(inflater, container, false);
        RVAccountMovementAdapter accountMovementAdapter = new RVAccountMovementAdapter();
        binding.rvAllAccountRevenue.setAdapter(accountMovementAdapter);
        tabRevenueViewModel.getMovements().observe(getViewLifecycleOwner(), accountMovementAdapter::setItems);

        this.binding.swpRefreshLayout.setOnRefreshListener(this::loadData);
        this.loadData();

        return binding.getRoot();
    }

    private void loadData(){
        this.binding.swpRefreshLayout.setRefreshing(true);
        this.compositeDisposable.add(
                tabRevenueViewModel.initialize()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> this.binding.swpRefreshLayout.setRefreshing(false),
                                throwable ->  {
                                    this.binding.swpRefreshLayout.setRefreshing(false);
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
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