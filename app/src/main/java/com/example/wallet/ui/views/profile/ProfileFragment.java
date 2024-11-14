package com.example.wallet.ui.views.profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentProfileBinding;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.models.UserUI;
import com.example.wallet.utils.LogErrorHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    NavController navController;
    ProfileViewModel profileViewModel;
    final CompositeDisposable disposable = new CompositeDisposable();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        this.disposable.add(
                this.profileViewModel.findUserSession()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {},
                                LogErrorHelper::print
                        )
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        BottomNavigationView bottomNavigationView = requireActivity()
                    .findViewById(R.id.nav_view);

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }

        if(this.profileViewModel != null){
            profileViewModel.getUserSession().observe(getViewLifecycleOwner(), userSession -> {
                this.binding.edtName.setText(userSession.getName());
                this.binding.edtLastName.setText(userSession.getLastName());
                this.binding.edtEmail.setText(userSession.getEmail());
                this.binding.edtPassword.setText(userSession.getPassword());
            });
        }

        this.binding.btnCancel.setOnClickListener(__ -> this.closeView());
        this.binding.btnSave.setOnClickListener(__ -> this.saveUser());

        this.binding.btnLogOut.setOnClickListener(__ -> this.handlerLogOut());
    }
    private void handlerLogOut(){
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingLogOut");
         this.disposable.add(
                 this.profileViewModel.logOut()
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(
                                 () -> {
                                     loading.dismiss();
                                     this.exitApp();
                                 },
                                 throwable -> {
                                     loading.dismiss();
                                     LogErrorHelper.print(throwable);
                                 }
                         )
         );
    }
    private void exitApp(){
        try {
            requireActivity().finish();
        } catch (Exception e) {
            LogErrorHelper.print(e);
        }
    }

    private void saveUser(){
        try {
            UserUI user = this.getUserView();
            if(!user.isValidUpdate()){
                Toast.makeText(getContext(), "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            }

            if(user.isValidUpdate()){
                this.executeUpdate(user);
            }
        } catch (Exception e){
            LogErrorHelper.print(e);
        }
    }

    private void executeUpdate(UserUI user){
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingUpdate");

        this.disposable.add(
                this.profileViewModel.updateUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Exito al actualizar", Toast.LENGTH_SHORT).show();
                                },
                                throwable ->{
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private UserUI getUserView(){
        UserUI userUI = new UserUI();
        userUI.setName(this.binding.edtName.getText().toString());
        userUI.setLastName(this.binding.edtLastName.getText().toString());
        userUI.setEmail(this.binding.edtEmail.getText().toString());
        userUI.setPassword(this.binding.edtPassword.getText().toString());
        return userUI;
    }

    private void closeView(){
        this.navController.popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }
}