package com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.R;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.models.Image;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.views.CornerLayout;

public class CustomImageSelectAdapter extends CustomGenericAdapter<Image> {

    public CustomImageSelectAdapter(Activity activity, Context context, ArrayList<Image> images) {
        super(activity, context, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_image, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.view = convertView.findViewById(R.id.iTooty);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;


        if (arrayList.get(position).isSelected) {
            viewHolder.view.setBorderColor(context.getResources().getColor(R.color.mainColor));
        } else {
            viewHolder.view.setBorderColor(context.getResources().getColor(R.color.transparent));
//            ((FrameLayout) convertView).setForeground(null);
        }

        Uri uri = Uri.fromFile(new File(arrayList.get(position).path));

        Glide.with(context).load(uri)
                .placeholder(R.color.colorAccent)
                .override(200, 200)
                .centerCrop()
                .into(viewHolder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public CornerLayout view;
    }

}
