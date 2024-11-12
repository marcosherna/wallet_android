package com.example.wallet.ui.views.home.tabs.revenue;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentTabRevenueBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

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

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RVAccountMovementAdapter accountMovementAdapter = new RVAccountMovementAdapter();
        binding.rvAllAccountRevenue.setAdapter(accountMovementAdapter);

        if(this.tabRevenueViewModel != null && !this.tabRevenueViewModel.isLoadData) {
            this.loadData();
            this.tabRevenueViewModel.isLoadData = true;
        }

        if(this.tabRevenueViewModel != null){

            tabRevenueViewModel.getMovements()
                    .observe(getViewLifecycleOwner(), accountMovementAdapter::setItems);
        }

        this.binding.swpRefreshLayout.setOnRefreshListener(this::loadData);

    }

    private void loadData(){
        try {
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
        } catch (Exception e){
            Log.println(Log.ERROR, "loadDataError -> tabRevenueView", "" );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        compositeDisposable.clear();
    }

}