package com.example.wallet.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.wallet.R;
import com.example.wallet.databinding.ItemTipVideoBinding;
import com.example.wallet.ui.models.TipUI;

import java.util.ArrayList;
import java.util.List;

public class RVTipAdapter extends RecyclerView.Adapter<RVTipAdapter.VideoViewHolder> {

    private List<TipUI> tipsList;
    private onListenerTipClick listener;

    public RVTipAdapter() {
        this.tipsList = new ArrayList<>();
    }

    // Método para actualizar la lista de tips
    public void setTipsList(List<TipUI> tipsList) {
        this.tipsList = tipsList;
        notifyDataSetChanged();
    }

    // Configurar el listener del clic
    public void setOnListenerTipClick(onListenerTipClick listener) {
        this.listener = listener;
    }

    // Interfaz para manejar el clic
    public interface onListenerTipClick {
        void onClick(TipUI tipUI);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tip_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        TipUI tip = tipsList.get(position);
        holder.render(tip);

        // Configurar el clic en el item
        holder.itemView.setOnClickListener(__ -> {
            if (this.listener != null) {
                this.listener.onClick(tip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tipsList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        private final ItemTipVideoBinding binding;

        public VideoViewHolder(View view) {
            super(view);
            binding = ItemTipVideoBinding.bind(view);
        }

        public void render(TipUI tip) {
            binding.tvTitle.setText(tip.getTitle());
            binding.tvDescription.setText(tip.getDescription());
            Glide.with(binding.imgThumbnail.getContext())
                    .load(tip.getThumbnailUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imgThumbnail);
        }
    }
}
