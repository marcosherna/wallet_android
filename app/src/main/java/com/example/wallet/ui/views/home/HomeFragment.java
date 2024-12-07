package com.example.wallet.ui.views.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.databinding.FragmentHomeBinding;
import com.example.wallet.ui.adapters.VPAdapter;
import com.example.wallet.ui.views.home.tabs.all.TabAllFragment;
import com.example.wallet.ui.views.home.tabs.expense.TabExpenseFragment;
import com.example.wallet.ui.views.home.tabs.plan.TabPlanFragment;
import com.example.wallet.ui.views.home.tabs.revenue.TabRevenueFragment;
import com.example.wallet.utils.TabHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private final ArrayList<Fragment> tabs = new ArrayList<>(Arrays.asList(
            new TabAllFragment(),
            new TabExpenseFragment(),
            new TabRevenueFragment(),
            new TabPlanFragment()
    ));

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // El HomeViewModel se usará como parent viewmodel para las tabs
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TabHelper tabHelper = new TabHelper(this, binding.tabLayout, binding.viewPager);
        this.tabs.forEach(tabHelper::setTab);
        VPAdapter adapter = new VPAdapter(this);
        tabHelper.setupTabLayoutAndViewPager(adapter);

        tabHelper.setOnCustomTabSelectedListener(tab -> {
            // Ocultar header en la pestaña Plan
            int visibility = tab.getPosition() == 3 ? View.GONE : View.VISIBLE;
            binding.headerContainer.setVisibility(visibility);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
