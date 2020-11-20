package com.prakash.ghimire.project.musicplayerapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.prakash.ghimire.project.musicplayerapp.R;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView musicImage;
        TextView musicName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            musicImage = itemView.findViewById(R.id.musicImage);
            musicName = itemView.findViewById(R.id.musicName);
        }
    }
}
