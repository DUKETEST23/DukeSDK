package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.model.UserLoadsModel;
import com.dukeai.manageloads.utils.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class LoadsListAdapter extends RecyclerView.Adapter<LoadsListAdapter.LoadsHolder> {

    private Context context;
    private List<UserLoadsModel> list;
    private OnListItemClickListener onListItemClickListener;
    DateFormatter dateFormatter = new DateFormatter();
    int green = Color.parseColor("#34C759");
    int lightGray = Color.parseColor("#AAAAAA");
    int lightRed = Color.parseColor("#FF453A");

    public LoadsListAdapter(Context context, OnListItemClickListener onListItemClickListener) {
        this.context = context;
        this.list = new ArrayList<>();
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public LoadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.loads_list_item,parent,false);
        return new LoadsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadsHolder holder, int position) {
        UserLoadsModel currentItem  = list.get(position);
        holder.checkBox.setChecked(list.get(position).isChecked());

        //TODO this code is written for differentiate cards on the basis of their status_
        if(currentItem.getStatus()==null){
            holder.loadListItem.setBackgroundColor(0);
        }else if(currentItem.getStatus().equals("invoiced")){
            holder.loadListItem.setBackgroundColor(lightGray);
        }else if(currentItem.getStatus().equals("paid")){
            holder.loadListItem.setBackgroundColor(green);
        }else if(currentItem.getStatus().equals("rejected")){
            holder.loadListItem.setBackgroundColor(lightRed);
        }else{
            holder.loadListItem.setBackgroundColor(0);
        }

        holder.loadId.setText(currentItem.getLoadId());
//        holder.uploadDate.setText(data.getCreatedDate());
//        holder.uploadDate.setText(dateFormatter.getFormattedDate(setRandomDates(position)));
        holder.uploadDate.setText(dateFormatter.getFormattedDate(currentItem.getDateCreated()));
//        holder.loadAmount.setText("NA");
        if (currentItem.getAmount()==null || currentItem.getAmount().equals("")) {
            holder.loadAmount.setText("NA");
        } else {
            holder.loadAmount.setText(String.valueOf(currentItem.getAmount()));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked) {
                    holder.checkBox.setPressed(false);
                    currentItem.setChecked(true);
                    if(!Duke.selectedLoadsForTransmission.contains(currentItem.getLoadUuid())){
                        Duke.selectedLoadsForTransmission.add(currentItem.getLoadUuid());
                    }else{
                        Duke.selectedLoadsForTransmission.remove(currentItem.getLoadUuid());
                    }
                } else {
                    holder.checkBox.setPressed(true);
                    currentItem.setChecked(false);
                    if(Duke.selectedLoadsForTransmission.contains(currentItem.getLoadUuid())){
                        Duke.selectedLoadsForTransmission.remove(currentItem.getLoadUuid());
                    }else{
                        Duke.selectedLoadsForTransmission.add(currentItem.getLoadUuid());
                    }
                }
                /*if(checked) {
                    if(!Duke.selectedLoadsForTransmission.contains(currentItem.getLoadUuid())){
                        Duke.selectedLoadsForTransmission.add(currentItem.getLoadUuid());
                    }
                } else {
                    if(Duke.selectedLoadsForTransmission.contains(currentItem.getLoadUuid())){
                        Duke.selectedLoadsForTransmission.remove(currentItem.getLoadUuid());
                    }
                }*/
            }
        });


        if(position == getItemCount()-1) {
            holder.separatorLine.setVisibility(View.GONE);
        }else{
            holder.separatorLine.setVisibility(View.VISIBLE);
        }

        holder.loadListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onListItemClick(position, currentItem, currentItem.getLoadUuid());
            }
        });
    }

    public void updateDataList(ArrayList<UserLoadsModel> dataList) {
        if (list != null) {
            list.clear();
            list.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onViewRecycled(LoadsHolder holder) {
        holder.checkBox.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LoadsHolder extends RecyclerView.ViewHolder {

        TextView loadId;
        TextView uploadDate;
        TextView loadAmount;
        CheckBox checkBox;
        LinearLayout loadListItem;
        View separatorLine;

        public LoadsHolder(@NonNull View itemView) {
            super(itemView);
            loadId = itemView.findViewById(R.id.load_id);
            uploadDate = itemView.findViewById(R.id.upload_date);
            loadAmount = itemView.findViewById(R.id.load_amount);
            checkBox = itemView.findViewById(R.id.is_load_selected);
            loadListItem = itemView.findViewById(R.id.load_list_item);
            separatorLine = itemView.findViewById(R.id.separator_line);
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int pos, UserLoadsModel dataModel,String loadUuid);
    }
}
