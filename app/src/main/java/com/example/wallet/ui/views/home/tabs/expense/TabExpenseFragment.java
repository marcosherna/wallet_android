package com.example.wallet.ui.views.home.tabs.expense;

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

import com.example.wallet.databinding.FragmentTabExpenseBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabExpenseFragment extends Fragment {

    FragmentTabExpenseBinding binding;
    TabExpenseViewModel tabExpenseViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tabExpenseViewModel = new ViewModelProvider(this).get(TabExpenseViewModel.class);
        binding = FragmentTabExpenseBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RVAccountMovementAdapter accountMovementAdapter = new RVAccountMovementAdapter();
        binding.rvAllAccountExpense.setAdapter(accountMovementAdapter);

        if(this.tabExpenseViewModel != null && !this.tabExpenseViewModel.isLoadData){
            this.loadData();
            this.tabExpenseViewModel.isLoadData = true;
        }

        if(this.tabExpenseViewModel != null){
            tabExpenseViewModel.getMovements().observe(getViewLifecycleOwner(), accountMovementAdapter::setItems);
        }

        binding.swpRefreshLayout.setOnRefreshListener(this::loadData);

    }

    private void loadData() {
        try {
            binding.swpRefreshLayout.setRefreshing(true);

            compositeDisposable.add(
                    tabExpenseViewModel.initialize()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> binding.swpRefreshLayout.setRefreshing(false),
                                    throwable -> {
                                        Toast.makeText(getContext(), "Error al cargar", Toast.LENGTH_SHORT).show();
                                        binding.swpRefreshLayout.setRefreshing(false);
                                    }
                            )
            );
        } catch (Exception e){
            Log.println(Log.ERROR, "loadDataError -> tabExpenseView", "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        this.compositeDisposable.clear();
    }

}