package com.DuAn1.techstore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Model.Photo;
import com.DuAn1.techstore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSlide extends RecyclerView.Adapter<AdapterSlide.PhotoViewHolder> {
    private final ArrayList<Photo> list;

    public AdapterSlide(ArrayList<Photo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = list.get(position);
        if (photo != null) {
            Picasso.get().load(photo.getPhoto())
                    .placeholder(R.drawable.ic_baseline_cached_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPhoto);
        }
    }
}
