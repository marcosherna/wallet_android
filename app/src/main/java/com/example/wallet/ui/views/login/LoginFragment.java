package com.example.wallet.ui.views.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.wallet.R;
import com.example.wallet.databinding.FragmentLoginBinding;
import com.example.wallet.ui.adapters.LoadingDialogFragment;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    NavController navController;
    LoginViewModel loginViewModel;
    final CompositeDisposable disposable = new CompositeDisposable();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        this.binding = FragmentLoginBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);

        this.binding.btnSession.setOnClickListener( __ -> this.handlerSessionClick());

        this.binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = String.valueOf(s);
                loginViewModel.setEmail(email);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = String.valueOf(s);
                loginViewModel.setPassword(password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.loginViewModel.getMessage().observe(getViewLifecycleOwner(),message -> {
            if(!message.isEmpty()){
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });


        this.loginViewModel.getHasUserSession().observe(getViewLifecycleOwner(), hasSession ->{
            if(hasSession){
                this.navigateHome();
            }
        });
    }

    private void handlerSessionClick() {
        try {
            LoadingDialogFragment loading = new LoadingDialogFragment();
            loading.show(requireActivity().getSupportFragmentManager(), "loadingLogin");
            disposable.add(
                    this.loginViewModel.login()
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
        } catch (Exception e){
            Log.println(Log.ERROR, "sessionError", "");
        }

    }

    private void navigateHome(){
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_login, true)
                .build();
        navController.navigate(R.id.navigation_home, null, navOptions);
    }

}
