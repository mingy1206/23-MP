package com.gachon.termproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private List<String> imageUrls;
    private Context context;
    private String url;
    public ImageAdapter(List<String> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
        Log.d("불러온 값", String.valueOf( this.imageUrls));
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        url = imageUrls.get(position);
        url = url.substring(1,url.length()-1);
        Glide.with(context)
                .load(url)
                .into(holder.imageView);
        Log.d("전달된 URL", String.valueOf(url));

    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
