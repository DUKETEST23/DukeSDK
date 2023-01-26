package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.UploadImagePreviewClickListener;
import com.dukeai.manageloads.utils.Utilities;

import java.util.ArrayList;


public class UploadImagesRecyclerViewAdapter extends RecyclerView.Adapter<UploadImagesRecyclerViewAdapter.ViewHolder> {

    Context context;
    int selectedPosition = 0;
    ViewHolder result;
    UploadImagePreviewClickListener uploadImagePreviewClickListener;
    private ArrayList<Bitmap> imagesList;
    private int resource;

    public UploadImagesRecyclerViewAdapter(ArrayList<Bitmap> imagesList, int resource, Context context, UploadImagePreviewClickListener listener) {
        this.imagesList = imagesList;
        this.resource = resource;
        this.context = context;
        this.uploadImagePreviewClickListener = listener;
        this.selectedPosition = imagesList.size() - 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(this.resource, viewGroup, false);
        result = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Bitmap data = imagesList.get(i);

        viewHolder.itemImage.setImageBitmap(data);
        if (selectedPosition == i) {
            viewHolder.view.setBackground(Utilities.getDrawable(context, R.drawable.selected_item_border));
        } else {
            viewHolder.view.setBackground(null);
        }
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadImagePreviewClickListener != null) {
                    selectedPosition = i;
                    uploadImagePreviewClickListener.onUploadImagePreviewClickListener(data);
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            itemImage = itemView.findViewById(R.id.item_image);
        }


    }
}
