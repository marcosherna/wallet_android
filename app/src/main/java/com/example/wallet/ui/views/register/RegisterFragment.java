package com.example.wallet.ui.views.register;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentRegisterBinding;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.models.UserUI;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterFragment extends Fragment {

    RegisterViewModel registerViewModel;
    FragmentRegisterBinding binding;
    final CompositeDisposable disposable = new CompositeDisposable();
    NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        this.binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        this.binding.txlLogin.setOnClickListener(__ -> navController.popBackStack());

        this.binding.btnRegister.setOnClickListener(__ -> this.handlerRegisterClick());

        this.registerViewModel.getIsCreated().observe(getViewLifecycleOwner(), isCreated -> {
            if (isCreated){
                this.navigateHome();
            }
        });
    }
    private void handlerRegisterClick(){
        try {
            UserUI user = this.getUser();
            if(!user.isValid()){
                Toast.makeText(getContext(), "Verifique", Toast.LENGTH_SHORT).show();
            } else {
                this.executeRegister(user);
            }
        } catch (Exception e){
            Log.println(Log.ERROR, "registerError", "");
        }
    }

    private void executeRegister(UserUI userUI){
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingRegisterUser");

        this.disposable.add(
                this.registerViewModel.registerUser(userUI)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                loading::dismiss,
                                throwable -> {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private UserUI getUser(){
        String name = this.binding.edtName.getText().toString();
        String lastName = this.binding.edtLastName.getText().toString();
        String email = this.binding.edtEmail.getText().toString();
        String password = this.binding.edtPassword.getText().toString();
        String confirmPassword = this.binding.edtConfirmPassword.getText().toString();
        return new UserUI(name,lastName, email, password, confirmPassword);
    }

    private void navigateHome(){
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.mobile_navigation, true)
                .build();

        navController.navigate(R.id.navigation_home, null, navOptions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
        this.disposable.clear();
    }
}