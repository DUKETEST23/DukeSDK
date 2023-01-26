package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.model.RecipientDataModel;

import java.util.ArrayList;

public class CustomRecipientListAdapter extends RecyclerView.Adapter<CustomRecipientListAdapter.CustomRecipientListHolder> {
    Context context;
    private ArrayList<RecipientDataModel> listdata;
    private OnListItemClickListener onListItemClickListener;
    private OnlastItemLoadedListener onlastItemLoadedListener;

    public CustomRecipientListAdapter( OnListItemClickListener onListItemClickListener, OnlastItemLoadedListener onlastItemLoadedListener) {
        this.listdata = new ArrayList<>();
        this.onListItemClickListener = onListItemClickListener;
        this.onlastItemLoadedListener = onlastItemLoadedListener;
    }

    @NonNull
    @Override
    public CustomRecipientListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_recipient_list_item, parent, false);
        CustomRecipientListAdapter.CustomRecipientListHolder viewHolder = new CustomRecipientListAdapter.CustomRecipientListHolder(listItem);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecipientListHolder holder, int position) {
        int realPos;
        if (getItemCount() == listdata.size()) {
            realPos = position == 1 ? listdata.size() - 2 : listdata.size() - 1;
        } else {
            realPos = position % listdata.size();
        }
        RecipientDataModel currentItem = listdata.get(realPos);
        holder.recipient.setText(currentItem.getEmail().toLowerCase());

        if (listdata.size() == 1 || listdata.size() == 2) {
            holder.edit.setAlpha(1.0f);
            holder.borderTop.setVisibility(View.INVISIBLE);
            holder.borderBottom.setVisibility(View.INVISIBLE);
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onListItemClick(realPos, currentItem);
            }
        });

        if (realPos == listdata.size() - 1) {
//            onlastItemLoadedListener.onLastItemLodedListener();
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size() > 2 ? Integer.MAX_VALUE : listdata.size();
    }

    public void updatelist(ArrayList<RecipientDataModel> selectedCustomRecipients) {
        this.listdata.clear();
        this.listdata = selectedCustomRecipients;
    }

    public static class CustomRecipientListHolder extends RecyclerView.ViewHolder {
        TextView recipient;
        ImageView edit;
        View borderTop;
        View borderBottom;

        public CustomRecipientListHolder(View itemView) {
            super(itemView);
            recipient = itemView.findViewById(R.id.recipient);
            edit = itemView.findViewById(R.id.edit_icon);
            borderTop = itemView.findViewById(R.id.border_top);
            borderBottom = itemView.findViewById(R.id.border_bottom);
        }

    }

    public interface OnListItemClickListener {
        void onListItemClick(int pos, RecipientDataModel dataModel);
    }

    public interface OnlastItemLoadedListener {
        void onLastItemLodedListener();
    }

}
