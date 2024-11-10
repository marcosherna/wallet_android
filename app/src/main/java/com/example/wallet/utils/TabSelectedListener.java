package com.example.wallet.utils;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class TabSelectedListener implements TabLayout.OnTabSelectedListener {



    private final ViewPager2 viewPager;

    public TabSelectedListener(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Acción opcional si necesitas manejar esto
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Acción opcional si necesitas manejar esto
    }
}