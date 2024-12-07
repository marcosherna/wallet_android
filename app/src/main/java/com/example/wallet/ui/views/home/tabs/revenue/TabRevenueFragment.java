package com.example.wallet.ui.views.home.tabs.revenue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.databinding.FragmentTabRevenueBinding;
import com.example.wallet.ui.adapters.RVAccountMovementAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TabRevenueFragment extends Fragment {
    private FragmentTabRevenueBinding binding; // generado a partir de fragment_tab_revenue
    private TabRevenueViewModel viewModel;
    private RVAccountMovementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTabRevenueBinding.inflate(inflater, container, false);

        adapter = new RVAccountMovementAdapter();
        binding.rvAllAccountRevenue.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TabRevenueViewModel.class);

        viewModel.initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        throwable -> {}
                );

        viewModel.getMovements().observe(getViewLifecycleOwner(), revenueMovements -> {
            adapter.setItems(revenueMovements);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
