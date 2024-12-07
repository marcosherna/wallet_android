package com.example.wallet.ui.views.home.tabs.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.databinding.FragmentTabExpenseBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabExpenseFragment extends Fragment {
    private FragmentTabExpenseBinding binding; // se generará a partir del nombre del layout fragment_tab_expense
    private TabExpenseViewModel viewModel;
    private RVAccountMovementAdapter adapter; // mismo adaptador que para All, ya que muestra movimientos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabExpenseBinding.inflate(inflater, container, false);

        adapter = new RVAccountMovementAdapter();
        binding.rvAllAccountExpense.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TabExpenseViewModel.class);

        viewModel.initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {/* Datos cargados con éxito */},
                        throwable -> {/* Manejar error */}
                );

        viewModel.getMovements().observe(getViewLifecycleOwner(), expenseMovements -> {
            adapter.setItems(expenseMovements);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
