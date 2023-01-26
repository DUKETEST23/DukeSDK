package com.dukeai.manageloads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.model.LoadsFilterDataModel;

import java.util.ArrayList;

public class LoadsFilterSpinnerAdapter extends ArrayAdapter implements SpinnerAdapter {

    Context context;
    int resource;
    ArrayList<LoadsFilterDataModel> data;

    public LoadsFilterSpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LoadsFilterDataModel> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LoadsFilterSpinnerHolder loadsFilterSpinnerHolder;
        View view = convertView;
        if (view == null) {
            loadsFilterSpinnerHolder = new LoadsFilterSpinnerHolder();
            LayoutInflater itemInflator = LayoutInflater.from(getContext());
            convertView = itemInflator.inflate(this.resource, parent, false);
            loadsFilterSpinnerHolder.spinnerText = convertView.findViewById(R.id.report_dropdown_item);
            loadsFilterSpinnerHolder.bottomBorder = convertView.findViewById(R.id.bottom_border);

            convertView.setTag(loadsFilterSpinnerHolder);
        } else {
            loadsFilterSpinnerHolder = (LoadsFilterSpinnerHolder) convertView.getTag();
        }
        LoadsFilterDataModel selectedItem = data.get(position);
        loadsFilterSpinnerHolder.spinnerText.setText(selectedItem.getSpinnerText());
        loadsFilterSpinnerHolder.bottomBorder.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public View getDropDownView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LoadsFilterSpinnerHolder loadsFilterSpinnerHolder = new LoadsFilterSpinnerHolder();
        LoadsFilterDataModel dataModel = data.get(position);
        LayoutInflater itemInflater = LayoutInflater.from(getContext());
        convertView = itemInflater.inflate(this.resource, parent, false);
        loadsFilterSpinnerHolder.spinnerText = convertView.findViewById(R.id.report_dropdown_item);
        loadsFilterSpinnerHolder.bottomBorder = convertView.findViewById(R.id.bottom_border);
        loadsFilterSpinnerHolder.dropdown = convertView.findViewById(R.id.dropdown_icon);

        loadsFilterSpinnerHolder.spinnerText.setText(dataModel.getSpinnerText());
        loadsFilterSpinnerHolder.spinnerText.setPadding(15, 15, 20, 15);
        loadsFilterSpinnerHolder.dropdown.setVisibility(View.GONE);
        loadsFilterSpinnerHolder.bottomBorder.setVisibility(View.GONE);

        convertView.setTag(loadsFilterSpinnerHolder);
        return convertView;
    }

    public class LoadsFilterSpinnerHolder {
        TextView spinnerText;
        View bottomBorder;
        ImageView dropdown;
    }

}
