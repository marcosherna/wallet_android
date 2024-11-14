package com.example.wallet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.wallet.domain.fake.date.LocalContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wallet.databinding.ActivityMainBinding;

import java.util.Objects;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocalContext.setup();

        RxJavaPlugins.setErrorHandler(e -> {
            Log.e("RxJavaError", "Unhandled exception", e);
        });


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
            try {
                if (destination.getId() == R.id.navigation_login ||
                        destination.getId() == R.id.navigation_register ||
                        destination.getId() == R.id.navigation_profile ||
                        destination.getId() == R.id.navigation_contact_me) {

                    navView.setVisibility(View.GONE);
                    Objects.requireNonNull(getSupportActionBar()).hide();
                } else {
                    navView.setVisibility(View.VISIBLE);
                    Objects.requireNonNull(getSupportActionBar()).show();
                }
            } catch (Exception e){

                Log.println(Log.ERROR, "Navigation",
                        "Error: " + e.getClass().getName() +" "+e.getMessage());
            }
        });

        if (!isLoggedIn) { // !isLoggedIn
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, true)
                    .build();

            navController.navigate(R.id.navigation_login, null, navOptions);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_session_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController.navigate(R.id.navigation_profile);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}