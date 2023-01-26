package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dukeai.manageloads.R;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.model.ProcessedDocumentsModel;
import com.dukeai.manageloads.utils.DateFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;


public class ProcessedDocumentsAdapter extends RecyclerView.Adapter<ProcessedDocumentsAdapter.MyViewHolder> {

    private static final int LIST_ITEM = 0;
    private static final int FOOTER_ITEM = 1;
    ArrayList<ProcessedDocumentsModel> list = new ArrayList<>();
    Context context;
    int resource;
    DateFormatter dateFormatter;
    private OnListItemClickListener onListItemClickListener;

    public ProcessedDocumentsAdapter(Context context, int resource, OnListItemClickListener onListItemClickListener) {
        this.context = context;
        this.resource = resource;
        this.onListItemClickListener = onListItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int currentResource = resource;

        if (viewType == FOOTER_ITEM) {
            currentResource = R.layout.empty_list_cell;
        }

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(currentResource, parent, false);

        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public int getItemViewType(int position) {

        if (position < list.size())
            return LIST_ITEM;
        else
            return FOOTER_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        /**Should not execute for Footer**/
        if (position < list.size()) {
            ProcessedDocumentsModel dataModel = list.get(position);
            Log.d("Data Model ", new Gson().toJson(dataModel));
            dateFormatter = new DateFormatter();
            String title = dataModel.getProcessedData().getTitle() + " " + dataModel.getSha1().substring(0, 2);
            String signature = dataModel.getSignature();

               /* if (dataModel.getProcessedData().getTitle().equals("Load Document")) {
                    holder.billName.setText(title);
                } else if (dataModel.getProcessedData().getDocType().toLowerCase().equals("scan") && dataModel.getSignature().length() < 1) {


                    if(signature != null){
                        Log.e("PM ","signature "+signature);
                    }

                    if(signature != null){
                        holder.billName.setText(signature);
                    }else{
                        holder.billName.setText(title);
                    }
                } else {
                    holder.billName.setText(dataModel.getProcessedData().getTitle());
                }*/

            if(signature != null){
               if(signature.equals("")){
                   holder.billName.setText(title);
               }else{
                   holder.billName.setText(signature);
               }
            }else{
                holder.billName.setText(title);
            }


             /*if (dataModel.getProcessedData() != null) {
                if (dataModel.getProcessedData().getTitle() != null)
                    if(dataModel.getProcessedData().getTitle().equals("Load Document")) {
                        holder.billName.setText(dataModel.getProcessedData().getTitle() + " " + dataModel.getSha1().substring(0, 2));
                    } else if(dataModel.getProcessedData().getDocType().toLowerCase().equals("scan") && dataModel.getSignature().length()<1) {
                        if(dataModel.getSignature() != null && dataModel.getSignature().length()>0){
                            holder.billName.setText(dataModel.getSignature());
                        }else{
                            holder.billName.setText(dataModel.getProcessedData().getTitle() + " " + dataModel.getSha1().substring(0, 2));
                        }
                    } else {
                        holder.billName.setText(dataModel.getProcessedData().getTitle());
                    }*/
            if (dataModel.getProcessedData().getDocType() != null)
                holder.billType.setText(dataModel.getProcessedData().getDocType());

            if (dataModel.getProcessedData().getNet() != null) {
//                    if(!Duke.isLoadDocument) {
//                        if(!dataModel.getProcessedData().getDocType().toLowerCase().equals("scan")) {
//                            holder.amount.setText(dataModel.getProcessedData().getNet().toString());
//                        } else {
//                            holder.amountUnits.setVisibility(View.INVISIBLE);
//                        }
//                    } else {
//                        holder.amountUnits.setVisibility(View.INVISIBLE);
//                    }
                if (!dataModel.getProcessedData().getTitle().equals("Load Document") && !dataModel.getProcessedData().getDocType().toLowerCase().equals("scan")) {
                    holder.amount.setText(dataModel.getProcessedData().getNet().toString());
                    holder.amount.setVisibility(View.VISIBLE);
                    holder.amountUnits.setVisibility(View.VISIBLE);
                } else {
                    holder.amount.setVisibility(View.INVISIBLE);
                    holder.amountUnits.setVisibility(View.INVISIBLE);
                }

//            }
        }

        if (dataModel.getProcessedData().getDocDate() != null)
            Log.e("Hey Doc Date ", "Signature " + signature + " Title " + dataModel.getProcessedData().getTitle() + " " + dataModel.getProcessedData().getDocDate() + " position " + position + " Before Date " + dataModel.getProcessedData().getDocDate());
        holder.processedDate.setText(dateFormatter.getFormattedDate(dataModel.getProcessedData().getDocDate()));

        if (position == getItemCount() - 1)
            holder.separatorLine.setVisibility(View.GONE);


            if (dataModel.getProcessedData().getTitle().equals("Load Document")) {
                holder.billName.setText(title);
            }
            holder.processedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListener.onListItemClick(position, dataModel);
            }
        });
    }
}

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void updateDataList(ArrayList<ProcessedDocumentsModel> dataList) {
        if (list != null) {
            list.clear();
            list.addAll(dataList);
            notifyDataSetChanged();
        }
    }

public interface OnListItemClickListener {
    void onListItemClick(int pos, ProcessedDocumentsModel dataModel);
}

public static class MyViewHolder extends RecyclerView.ViewHolder {
    View processedView;

    TextView billName;
    TextView billType;

//        @BindView(R.id.upload_date)
//        TextView uploadDate;

    TextView processedDate;
    TextView amount;

    TextView amountUnits;
    View separatorLine;

    public MyViewHolder(View itemView, int type) {
        super(itemView);
        if (type == LIST_ITEM) {
            processedView = itemView;

            billName = itemView.findViewById(R.id.bill_name);
            billType = itemView.findViewById(R.id.bill_type);
            processedDate = itemView.findViewById(R.id.processed_date);
            amount = itemView.findViewById(R.id.amount);
            amountUnits = itemView.findViewById(R.id.amount_units);
            separatorLine = itemView.findViewById(R.id.separator_line);

            ChangeThemeModel changeThemeModel = new ChangeThemeModel();
            amountUnits.setTextColor(Color.parseColor(changeThemeModel.getFontColor()));
            amount.setTextColor(Color.parseColor(changeThemeModel.getFontColor()));
        }
    }
}

public static class EmptyCellViewHolder extends RecyclerView.ViewHolder {

    public EmptyCellViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
}