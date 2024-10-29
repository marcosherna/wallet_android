package com.example.wallet.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.databinding.ItemAccountMovementBinding;
import com.example.wallet.domain.models.AccountMovement;

import java.util.ArrayList;

public class RVAccountMovementAdapter extends RecyclerView
        .Adapter<RVAccountMovementAdapter.AccountMovementHolder> {

    ArrayList<AccountMovement> itemsModels;
    public RVAccountMovementAdapter(ArrayList<AccountMovement> itemsModels){
        this.itemsModels = new ArrayList<>();
        this.itemsModels = itemsModels;
    }

    public static class AccountMovementHolder extends RecyclerView.ViewHolder {
        ItemAccountMovementBinding binding;
        public AccountMovementHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ItemAccountMovementBinding.bind(itemView);
        }

        public void render(AccountMovement item){
            String typeMovement = item.getTypeMovements() == AccountMovement.Type.EXPENSE ? "Egreso":"Ingreso";
            this.binding.tvTypeMovement.setText(typeMovement);
            this.binding.tvAmount.setText(item.getAmount().toString());
            this.binding.tvIdPlan.setText(item.getIdPlan());
        }
    }

    @NonNull
    @Override
    public AccountMovementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account_movement, parent, false);

        return new AccountMovementHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountMovementHolder holder, int position) {
        AccountMovement item = this.itemsModels.get(position);
        holder.render(item);
    }

    @Override
    public int getItemCount() {
        return this.itemsModels.size();
    }


}
