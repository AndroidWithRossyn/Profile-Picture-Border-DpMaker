package com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.R;

import java.util.List;

public class YourImageAdapter extends RecyclerView.Adapter<YourImageAdapter.ImageViewHolder> {

    private List<Integer>  imageList;
    private Context context;

    public YourImageAdapter(List<Integer> drawableList, Context context) {
        this.imageList = drawableList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_home_scroll, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        int imageResourceId = imageList.get(position);
        holder.imageView.setImageResource(imageResourceId);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}