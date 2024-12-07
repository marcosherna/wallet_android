package com.example.wallet.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.databinding.ItemAccountMovementBinding;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.ui.models.TypeAccountMovement;

import java.util.ArrayList;
import java.util.List;

public class RVAccountMovementAdapter extends RecyclerView
        .Adapter<RVAccountMovementAdapter.AccountMovementHolder> {

    List<AccountMovementUI> itemsModels;
    public RVAccountMovementAdapter( ){
        this.itemsModels = new ArrayList<>();
    }

    public void setItems(List<AccountMovementUI> itemsModels){
        if(this.itemsModels != null){
            this.itemsModels.clear();
            this.itemsModels = itemsModels;
            notifyDataSetChanged();
        }
    }

    public static class AccountMovementHolder extends RecyclerView.ViewHolder {
        ItemAccountMovementBinding binding;
        Context context;
        public AccountMovementHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ItemAccountMovementBinding.bind(itemView);
            this.context = itemView.getContext();
        }

        public void render(AccountMovementUI item){
            int color = item.getTypeAccountMovement() == TypeAccountMovement.REVENUE ? R.color.teal_700 : R.color.expense_color_dark;
            String typeMovement = item.getTypeAccountMovement() == TypeAccountMovement.EXPENSE ? "Egreso":"Ingreso";
            this.binding.tvTypeMovement.setText(typeMovement);
            this.binding.tvTypeMovement.setTextColor(context.getColor(color));
            this.binding.tvAmount.setText(item.getAmount());

            if (item.getAutoIncrementId() != null) {
                this.binding.tvIdPlan.setText(String.valueOf(item.getAutoIncrementId()));
            } else {
                this.binding.tvIdPlan.setText("N/A");
            }
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
        AccountMovementUI item = this.itemsModels.get(position);
        holder.render(item);
    }

    @Override
    public int getItemCount() {
        return this.itemsModels.size();
    }
}