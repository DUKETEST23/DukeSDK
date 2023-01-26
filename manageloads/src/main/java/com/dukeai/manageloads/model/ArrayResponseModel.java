package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ArrayResponseModel {
    @SerializedName("Code")
    String code;
    @SerializedName("Message")
    String message[];

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.message = list.toArray(new String[list.size()]);
    }
}
