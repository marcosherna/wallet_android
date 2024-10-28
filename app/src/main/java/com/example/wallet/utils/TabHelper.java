package com.example.wallet.utils;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wallet.ui.adapters.VPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

public class TabHelper {
    public static void setupTabLayoutAndViewPager(Fragment context, TabLayout tabLayout, ViewPager2 viewPager, ArrayList<Fragment> fragments) {
        VPAdapter vpAdapter = new VPAdapter(context);

        for (Fragment fragment : fragments) {
            vpAdapter.addFragment(fragment);
        }

        viewPager.setAdapter(vpAdapter);

        tabLayout.addOnTabSelectedListener(new TabSelectedListener(viewPager));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });
    }
}
