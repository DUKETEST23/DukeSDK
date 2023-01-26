package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.SelectPhotoListener;

import java.util.ArrayList;



public class UploadImageSelectionAdapter extends RecyclerView.Adapter<UploadImageSelectionAdapter.MyViewHolder> {

    Context context;
    int resource;
    private ArrayList<Bitmap> imageList = new ArrayList<>();
    private SelectPhotoListener selectPhotoListener;
    private int imageCount = 0;
    private int removedPosition = -1;
    private ArrayList<Integer> imageSelectedCount = new ArrayList<>();

    public UploadImageSelectionAdapter(Context context, int resource, ArrayList<Bitmap> list, SelectPhotoListener listener) {
        this.context = context;
        this.resource = resource;
        this.imageList = list;
        this.imageCount = 0;
        setInitials();
        this.selectPhotoListener = listener;
    }

    public UploadImageSelectionAdapter(Context context, int resource, SelectPhotoListener listener) {
        this.context = context;
        this.resource = resource;
        this.imageCount = 0;
        setInitials();
        this.selectPhotoListener = listener;
    }

    private void setInitials() {
        if (imageList == null || imageList.size() <= 0) {
            return;
        }
        this.imageSelectedCount.clear();
        for (int i = 0; i < imageList.size(); i++) {
            this.imageSelectedCount.add(i, 0);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(resource, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    public void updateImagesList(ArrayList<Bitmap> bitmaps) {
        this.imageCount = 0;
        this.imageList = bitmaps;
        setInitials();
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Bitmap data = imageList.get(i);
        myViewHolder.documentImage.setImageBitmap(data);

        if (imageSelectedCount != null && imageSelectedCount.size() > 0) {
            Integer value = imageSelectedCount.get(i);
            if (value != null && value > removedPosition && value != 0 && removedPosition != -1) {
                int count = value - 1;
                if (count != 0) {
                    imageSelectedCount.set(i, count);
                    myViewHolder.imageSelectionBackground.setBackground(context.getDrawable(R.drawable.image_selection_background));
                    myViewHolder.selectedImageCount.setText(imageSelectedCount.get(i).toString());
                }
            } else if (value == 0) {
                myViewHolder.imageSelectionBackground.setBackground(context.getDrawable(R.drawable.non_selected_image_background));
                myViewHolder.selectedImageCount.setText("");
            } else {
                myViewHolder.selectedImageCount.setText(imageSelectedCount.get(i).toString());
                myViewHolder.imageSelectionBackground.setBackground(context.getDrawable(R.drawable.image_selection_background));
            }
        } else {
            myViewHolder.selectedImageCount.setText("");
            myViewHolder.imageSelectionBackground.setBackground(context.getDrawable(R.drawable.non_selected_image_background));
        }


        if (i == (getItemCount())) {
            removedPosition = -1;
        }

        myViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPhotoListener != null) {
                    if (imageSelectedCount.get(i) == 0) {
                        imageCount++;
                        imageSelectedCount.set(i, imageCount);
                        myViewHolder.selectedImageCount.setVisibility(View.VISIBLE);
                        myViewHolder.imageSelectionBackground.setBackground(context.getDrawable(R.drawable.image_selection_background));
                        myViewHolder.selectedImageCount.setText(String.valueOf(imageCount));
                    } else {
                        removedPosition = imageSelectedCount.get(i);
                        if (imageCount > 0) {
                            imageCount--;
                        } else {
                            imageCount = 0;
                        }
                        v.setSelected(false);
                        imageSelectedCount.set(i, 0);
                        myViewHolder.imageSelectionBackground.setBackground(context.getDrawable(R.drawable.non_selected_image_background));
                        myViewHolder.selectedImageCount.setText("");
                        notifyDataSetChanged();
                    }
                    selectPhotoListener.onSelectPhoto(imageSelectedCount);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView documentImage;
        TextView selectedImageCount;
        RelativeLayout imageSelectionBackground;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
