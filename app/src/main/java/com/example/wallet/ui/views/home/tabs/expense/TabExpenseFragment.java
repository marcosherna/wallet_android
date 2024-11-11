package com.example.wallet.ui.views.home.tabs.expense;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.databinding.FragmentTabExpenseBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabExpenseFragment extends Fragment {

    FragmentTabExpenseBinding binding;
    TabExpenseViewModel tabExpenseViewModel;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    Disposable disposable;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tabExpenseViewModel = new ViewModelProvider(this).get(TabExpenseViewModel.class);
        binding = FragmentTabExpenseBinding.inflate(inflater, container, false);

        RVAccountMovementAdapter accountMovementAdapter = new RVAccountMovementAdapter();
        binding.rvAllAccountExpense.setAdapter(accountMovementAdapter);

        tabExpenseViewModel.getMovements().observe(getViewLifecycleOwner(), accountMovementAdapter::setItems);
        binding.swpRefreshLayout.setOnRefreshListener(this::loadData);

        this.loadData();
        return binding.getRoot();
    }

    private void loadData() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        this.compositeDisposable.clear();
    }

}