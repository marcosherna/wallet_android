package com.example.wallet.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.databinding.ItemAccountMovementCheckSelectorBinding;
import com.example.wallet.domain.models.AccountMovement;
import com.example.wallet.ui.models.AccountMovementUI;
import com.example.wallet.utils.DateFormatHelper;

import java.util.ArrayList;
import java.util.List;

public class RVAccountMovementWithCheck extends RecyclerView.Adapter<RVAccountMovementWithCheck.AccountMovementWithCheckHolder> {

    ArrayList<AccountMovementUI> movements;

    public RVAccountMovementWithCheck(){
        this.movements = new ArrayList<>();
    }

    public void addMovement(AccountMovementUI movement){
        this.movements.add(movement);
    }

    public List<AccountMovementUI> getItems(){
        return this.movements;
    }

    @NonNull
    @Override
    public AccountMovementWithCheckHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account_movement_check_selector, parent, false);

        return new AccountMovementWithCheckHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountMovementWithCheckHolder holder, int position) {
        AccountMovementUI movement = this.movements.get(position);
        holder.render(movement);
    }

    @Override
    public int getItemCount() {
        return this.movements.size();
    }

    public void checkAll(boolean isCheck){
        this.movements.forEach( movement -> {
            movement.setCheck(isCheck);
        });
        notifyItemRangeChanged(0, movements.size());
    }

    public static class  AccountMovementWithCheckHolder extends RecyclerView.ViewHolder {
        ItemAccountMovementCheckSelectorBinding binding;
        public AccountMovementWithCheckHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ItemAccountMovementCheckSelectorBinding.bind(itemView);

        }

        public void render(AccountMovementUI movement){
            this.binding.cbMovement.setChecked(movement.isCheck());
            this.binding.tvAmount.setText(movement.getAmount());
            this.binding.tvDate.setText(movement.getDate());
            this.binding.tvIdPlan.setText(movement.getIdPlan());

            this.binding.cbMovement.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                movement.setCheck(isChecked);
            }));
        }
    }

}
