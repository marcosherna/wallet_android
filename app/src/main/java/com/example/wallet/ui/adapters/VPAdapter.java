package com.example.wallet.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> lstFragments = new ArrayList<>();

    public VPAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.lstFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return this.lstFragments.size();
    }
    public void addFragment(Fragment fragment) {
        this.lstFragments.add(fragment);
    }
}