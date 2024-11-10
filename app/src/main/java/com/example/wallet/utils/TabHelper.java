package com.example.wallet.utils;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wallet.ui.adapters.VPAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TabHelper {
    OnCustomTabSelectedListener listener;
    public interface  OnCustomTabSelectedListener{
        void OnTabSelected(TabLayout.Tab tab);
    }
    public void setOnCustomTabSelectedListener(OnCustomTabSelectedListener listener){
        this.listener = listener;
    }

    Fragment context;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    List<Fragment> tabs;

    public TabHelper(Fragment context, TabLayout tabLayout, ViewPager2 viewPager2) {
        this.context = context;
        this.tabLayout = tabLayout;
        this.viewPager2 = viewPager2;
        tabs = new ArrayList<>();
    }

    public void setTab(Fragment tab){
        this.tabs.add(tab);
    }


    public void setupTabLayoutAndViewPager(VPAdapter adapter) {

        tabs.forEach(adapter::addFragment);
        viewPager2.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                if(listener != null){
                    listener.OnTabSelected(tab);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

    }
}
