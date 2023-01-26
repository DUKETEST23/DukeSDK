package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.model.SelectRecipientDataModel;

import java.util.ArrayList;

public class SelectRecipientAdapter extends RecyclerView.Adapter<SelectRecipientAdapter.SelectRecipientListHolder> {

    Context context;
    private ArrayList<SelectRecipientDataModel> listdata;
    private OnListItemClickListener onListItemClickListener;


    public SelectRecipientAdapter(Context context, ArrayList<SelectRecipientDataModel> listdata, OnListItemClickListener onListItemClickListener) {
        this.context = context;
        this.listdata = listdata;
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public SelectRecipientListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem = layoutInflater.inflate(R.layout.layout_select_recipient_item, parent, false);
//        SelectRecipientAdapter.SelectRecipientListHolder viewHolder = new SelectRecipientAdapter.SelectRecipientListHolder(listItem);
//        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.layout_select_recipient_item,parent,false);

        return new SelectRecipientListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectRecipientListHolder holder, int position) {
        SelectRecipientDataModel currentItem = listdata.get(position);
        holder.recipientEmail.setText(currentItem.getRecipientEmail().toLowerCase());
        holder.checkBox.setChecked(currentItem.isSelected);

       /* if(currentItem.isSelected){
            Log.e("Selected"," "+currentItem.getRecipientEmail());
            holder.checkBox.setChecked(currentItem.isSelected);
        }else{
            Log.e("Unselected"," "+currentItem.getRecipientEmail());
//            holder.checkBox.setChecked(false);
            holder.checkBox.setChecked(currentItem.isSelected);
        }*/

        holder.recipientEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                } else {
                    holder.checkBox.setChecked(true);
                }
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    currentItem.setSelected(true);
                } else {
                    currentItem.setSelected(false);
                }
                onListItemClickListener.onListItemClick(position, currentItem);
            }
        });
//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onListItemClickListener.onListItemClick(position, currentItem);
//            }
//        });
    }

    @Override
    public void onViewRecycled(@NonNull SelectRecipientListHolder holder) {
        holder.checkBox.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public interface OnListItemClickListener {
        void onListItemClick(int pos, SelectRecipientDataModel dataModel);
    }

    public static class SelectRecipientListHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView recipientEmail;

        public SelectRecipientListHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.is_recipient_selected_checkbox);

            recipientEmail = itemView.findViewById(R.id.recipient_email);
        }

    }
}
