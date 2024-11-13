package com.example.wallet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.wallet.domain.fake.date.LocalContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wallet.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalContext.setup();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_manage, R.id.navigation_plan, R.id.navigation_tip)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if (destination.getId() == R.id.navigation_login ||
                    destination.getId() == R.id.navigation_register) {

                navView.setVisibility(View.GONE);
                getSupportActionBar().hide();
            } else {
                navView.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
            }
        });

        if (!isLoggedIn) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, true)
                    .build();

            navController.navigate(R.id.navigation_login, null, navOptions);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}