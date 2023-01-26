package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class UploadFileItemAdapter extends RecyclerView.Adapter<UploadFileItemAdapter.MyViewHolder> {

    ArrayList<Bitmap> images = new ArrayList<>();
    int resource;
    Context parentContext;

    public UploadFileItemAdapter(Context context, int resource, ArrayList<Bitmap> list) {
        this.parentContext = context;
        this.resource = resource;
        this.images = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(resource, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Bitmap data = images.get(i);
        myViewHolder.fileImage.setImageBitmap(data);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView fileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
