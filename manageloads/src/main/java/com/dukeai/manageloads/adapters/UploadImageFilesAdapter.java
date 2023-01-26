package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dukeai.manageloads.R;

import java.util.ArrayList;


public class UploadImageFilesAdapter extends RecyclerView.Adapter<UploadImageFilesAdapter.MyViewHolder> {

    Context context;
    int resource;
    ArrayList<ArrayList<Bitmap>> imageList = new ArrayList<>();
    UploadFileItemAdapter adapter;


    public UploadImageFilesAdapter(Context context, int res, ArrayList<ArrayList<Bitmap>> list) {
        this.context = context;
        this.resource = res;
        this.imageList = list;
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
        final ArrayList<Bitmap> data = imageList.get(i);

        myViewHolder.uploadFileText.setText("file " + (i + 1));
        adapter = new UploadFileItemAdapter(context, R.layout.layout_upload_file_document, data);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        myViewHolder.imageRecycler.setLayoutManager(layoutManager);
        myViewHolder.imageRecycler.setHasFixedSize(true);
        myViewHolder.imageRecycler.setItemAnimator(new DefaultItemAnimator());
        myViewHolder.imageRecycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView uploadFileText;
        RecyclerView imageRecycler;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
