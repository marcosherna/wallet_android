package com.example.wallet.ui.adapters;

import android.content.Intent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RVTipAdapter extends RecyclerView.Adapter<RVTipAdapter.VideoViewHolder> {

    private List<TipUI> tipsList;
    private onListenerTipClick listener;

    public RVTipAdapter() {
        this.tipsList = new ArrayList<>();
    }

    public void setTipsList(List<TipUI> tipsList){
        this.tipsList = tipsList;
        notifyDataSetChanged();
    }
    public void setOnListenerTipClick(onListenerTipClick listener){
        this.listener = listener;
    }
    public interface onListenerTipClick{
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

        holder.itemView.setOnClickListener( __ -> {
            if(this.listener != null){
                this.listener.onClick(tip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.tipsList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ItemTipVideoBinding binding;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = ItemTipVideoBinding.bind(itemView);
        }

        public void render(TipUI tipUI){
            this.binding.tvTitle.setText(tipUI.getTitle());
            this.binding.tvDescription.setText(tipUI.getDescription());

            Glide.with(itemView.getContext())
                    .load(tipUI.getThumbnailUrl())
                    .placeholder(R.drawable.ic_manager_black_24)
                    .error(R.drawable.ic_notifications_black_24dp)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this.binding.imgThumbnail);
        }

//        private String extractYoutubeId(String youtubeUrl) {
//            String result = "";
//            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed\\/|%2F|watch\\?v=|%2Fembed%2F|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|%3D|\\/|%2F|embed\\/|%2F|watch\\?v=|%2Fembed%2F|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v=|%2F|watch\\?v=)([^#&?]*).*";
//            Pattern compiledPattern = Pattern.compile(pattern);
//            Matcher matcher = compiledPattern.matcher(youtubeUrl);
//            if (matcher.find()) {
//                result = matcher.group(1);
//            }
//            return result;
//        }
    }


}