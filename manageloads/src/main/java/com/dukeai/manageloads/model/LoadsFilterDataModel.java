package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoadsFilterDataModel implements Serializable {

    @SerializedName("spinnerText")
    String spinnerText;

    public LoadsFilterDataModel(String spinnerText) {
        setSpinnerText(spinnerText);
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public void setSpinnerText(String spinnerText) {
        this.spinnerText = spinnerText;
    }
}
