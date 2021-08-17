package com.prakash.ghimire.project.musicplayerapp.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.prakash.ghimire.project.musicplayerapp.R;
import com.prakash.ghimire.project.musicplayerapp.model.Music;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{
    private Context context;
    private List<Music> list;
    private ViewHolder.OnItemClickListener itemClickListener;

    public MusicAdapter(Context context, List<Music> list, ViewHolder.OnItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item_layout, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Music getPosition = list.get(position);

        byte[] image = getAlbumArt(getPosition.getPath());
        if (image == null){
            holder.defaultMusicAlbumCard.setVisibility(View.VISIBLE);
        }else {
            Picasso.get().load(Arrays.toString(image)).into(holder.musicAlbum);
        }

        holder.musicTitle.setText(getPosition.getTitle());
        holder.musicArtist.setText(getPosition.getArtist());
        holder.musicDuration.setText(String.format("duration:%s", getPosition.getDuration()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MaterialCardView defaultMusicAlbumCard;
        ImageView musicAlbum;
        TextView musicTitle, musicArtist, musicDuration;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            listener = mListener;

            defaultMusicAlbumCard = itemView.findViewById(R.id.defaultMusicAlbumCard);
            musicAlbum = itemView.findViewById(R.id.musicAlbum);
            musicTitle = itemView.findViewById(R.id.musicTitle);
            musicArtist = itemView.findViewById(R.id.musicArtist);
            musicDuration = itemView.findViewById(R.id.musicDuration);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.OnItemClick(getAdapterPosition());
            }
        }

        public interface OnItemClickListener{
            void OnItemClick(int position);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(uri);
        } catch (Exception e){
            e.printStackTrace();
        }

        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
