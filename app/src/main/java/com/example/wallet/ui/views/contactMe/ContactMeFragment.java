package com.example.wallet.ui.views.contactMe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.wallet.databinding.FragmentContactMeBinding;
import com.example.wallet.ui.adapters.LoadingDialogFragment;
import com.example.wallet.ui.models.ContactUI;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContactMeFragment extends Fragment {
    FragmentContactMeBinding binding;
    ContactMeViewModel contactMeViewModel;
    NavController navController;
    final CompositeDisposable disposable = new CompositeDisposable();
    private ActivityResultLauncher<Intent> mGetContent;
    ContactUI contactUI;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.contactMeViewModel = new ViewModelProvider(this).get(ContactMeViewModel.class);
        this.binding = FragmentContactMeBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);
        this.contactUI = new ContactUI();
        this.binding.btnSend.setOnClickListener(__ -> this.handlerSendClick());
        this.binding.btnCancel.setOnClickListener(__ -> this.handlerBackNavigateClick());
    }

    private boolean verifyPermissions(){
        return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }
    private void handlerBackNavigateClick(){
        this.navController.popBackStack();
    }
    private void handlerSendClick(){
        ContactUI contactUI = this.getContactView();
        if(!contactUI.isValid()){
            Toast.makeText(getContext(), "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
        }

        if(contactUI.isValid()){
            LoadingDialogFragment loading = new LoadingDialogFragment();
            loading.show(getParentFragmentManager(), "loadingSend");

            this.disposable.add(
                    this.contactMeViewModel.send()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        this.clearContactView();
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                                    },
                                    throwable -> {
                                        loading.dismiss();
                                        Toast.makeText(getContext(), "Error al enviar", Toast.LENGTH_SHORT).show();
                                    }
                            )
            );
        }
    }

    private ContactUI getContactView(){
        contactUI.setWhoWrite(this.binding.edtWho.getText().toString());
        contactUI.setWhichReason(this.binding.edtWhich.getText().toString());
        contactUI.setDetail(this.binding.edtDetail.getText().toString());
        return  contactUI;
    }

    private void clearContactView(){
        this.binding.edtWho.setText("");
        this.binding.edtWhich.setText("");
        this.binding.edtDetail.setText("");
        this.contactUI.clear();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
        this.disposable.clear();
    }
}