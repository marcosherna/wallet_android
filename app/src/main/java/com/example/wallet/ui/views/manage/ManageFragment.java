package com.example.wallet.ui.views.manage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentManageBinding;
import com.example.wallet.domain.models.AccountMovement;

public class ManageFragment extends Fragment {

    FragmentManageBinding binding;

    NavController navController;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        ManageViewModel manageViewModel = new ViewModelProvider(this).get(ManageViewModel.class);
        binding = FragmentManageBinding.inflate(inflater, container, false);




        binding.btnAddRevenue.setOnClickListener(NavigateToAddCountMovementFragment("Ingresos", AccountMovement.Type.REVENUE));
        binding.btnAddExpense.setOnClickListener(NavigateToAddCountMovementFragment("Egresos", AccountMovement.Type.EXPENSE));

        return binding.getRoot();
    }

    private View.OnClickListener NavigateToAddCountMovementFragment(String title, AccountMovement.Type typeMovement){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putSerializable("typeMovement", typeMovement);
        return view -> navController.navigate(R.id.navigation_manage_to_navigation_add_account, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}