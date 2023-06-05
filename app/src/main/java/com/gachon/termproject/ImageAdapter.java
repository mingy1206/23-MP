package com.gachon.termproject;
//Grilde를 이용한 이미지 띄우기
//출처 : https://stackoverflow.com/questions/56680173/glide-not-able-to-load-some-of-images-failed-with-exception
import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }


    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        url = imageUrls.get(position);
        Log.d("~~~~~~~~~~~~~",url);
        Glide.with(context)
                .load(url)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof GalleryActivity) {
                    int clickedPosition = holder.getAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        String clickedImageUrl = imageUrls.get(clickedPosition);
                        Intent val = new Intent();
                        val.putExtra("return",clickedImageUrl);
                        ((GalleryActivity) context).setResult(Activity.RESULT_OK,val);
                        Log.d("clickedImageUrl", clickedImageUrl);

                        archive fragment = (archive) ((GalleryActivity) context).getSupportFragmentManager().findFragmentByTag("2");
                        ((GalleryActivity) context).finish();
                    }
                }
            }


        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
