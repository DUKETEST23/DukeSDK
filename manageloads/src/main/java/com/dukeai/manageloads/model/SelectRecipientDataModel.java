package com.dukeai.manageloads.model;

public class SelectRecipientDataModel {
    public boolean isSelected;
    public String recipientEmail;

    public SelectRecipientDataModel(boolean isSelected, String recipientEmail) {
        this.isSelected = isSelected;
        this.recipientEmail = recipientEmail;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}
