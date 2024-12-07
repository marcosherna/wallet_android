package com.example.wallet.ui.views.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    NavController navController;
    LoginViewModel loginViewModel;
    final CompositeDisposable disposable = new CompositeDisposable();
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Request code para Google SignIn

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        // Configurar GoogleSignInOptions con tu client_id
        // Asegúrate que default_web_client_id esté definido en strings.xml
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        // Botón Login con email/contraseña
        binding.btnSession.setOnClickListener(__ -> handlerSessionClick());

        // Observadores de texto
        binding.edtEmail.addTextChangedListener(new SimpleTextWatcher(s -> loginViewModel.setEmail(s)));
        binding.edtPassword.addTextChangedListener(new SimpleTextWatcher(s -> loginViewModel.setPassword(s)));

        loginViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getHasUserSession().observe(getViewLifecycleOwner(), hasSession -> {
            if (hasSession) {
                navigateHome();
            }
        });

        binding.txlCreateNewUser.setOnClickListener(__ -> handlerNavigateCreateUserClick());

        // Botón de Iniciar con Google
        binding.btnGoogleSignIn.setSize(SignInButton.SIZE_STANDARD);
        binding.btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
    }

    private void handlerNavigateCreateUserClick() {
        navController.navigate(R.id.navigation_to_register, null);
    }

    private void handlerSessionClick() {
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingLogin");
        disposable.add(
                loginViewModel.login()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Login exitoso", Toast.LENGTH_SHORT).show();
                                },
                                throwable -> {
                                    loading.dismiss();
                                    Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    private void signInWithGoogle() {
        // Limpiar cualquier sesión previa (opcional)
        // Si deseas que siempre pregunte la cuenta:
        // mGoogleSignInClient.signOut(); // para forzar elegir otra cuenta de Google

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            // Resultado de iniciar SignIn con Google
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch(ApiException e) {
                Toast.makeText(getContext(), "Fallo en Google SignIn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        LoadingDialogFragment loading = new LoadingDialogFragment();
        loading.show(requireActivity().getSupportFragmentManager(), "loadingGoogleLogin");

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    loading.dismiss();
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Aquí el usuario ya está creado/enlazado con su cuenta de Google en FirebaseAuth.
                            loginViewModel.setUserSession(true);
                            navigateHome();
                        }
                    } else {
                        Toast.makeText(getContext(), "Autenticación con Google falló", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateHome() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.navigation_login, true)
                .build();
        navController.navigate(R.id.navigation_home, null, navOptions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        disposable.clear();
    }

    // Cerrar sesión completo (puedes llamarlo desde un botón en otro fragment)
    private void signOut() {
        // Cerrar sesión en FirebaseAuth
        FirebaseAuth.getInstance().signOut();
        // Cerrar sesión en Google
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            Toast.makeText(getContext(), "Sesión cerrada completamente", Toast.LENGTH_SHORT).show();
        });
    }

    // Clase helper para el TextWatcher
    private static class SimpleTextWatcher implements TextWatcher {
        private final Consumer<String> consumer;
        SimpleTextWatcher(Consumer<String> consumer){
            this.consumer = consumer;
        }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            consumer.accept(s.toString());
        }
        @Override public void afterTextChanged(Editable s) {}
    }

    interface Consumer<T>{
        void accept(T t);
    }
}
