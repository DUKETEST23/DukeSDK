package com.dukeai.manageloads.model;

public class ItemToDisplay {
    private String labelText;
    private String dataText;
    private String messageText;

    private int labelColor;
    private int dataColor;
    private int messageColor;

    private int dataBackground;

    private String dataDrawable;

    public ItemToDisplay(String labelText, String dataText, String messageText, int labelColor, int dataColor, int messageColor, int dataBackground, String dataDrawable) {
        this.labelText = labelText;
        this.dataText = dataText;
        this.messageText = messageText;
        this.labelColor = labelColor;
        this.dataColor = dataColor;
        this.messageColor = messageColor;
        this.dataBackground = dataBackground;
        this.dataDrawable = dataDrawable;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public int getDataColor() {
        return dataColor;
    }

    public void setDataColor(int dataColor) {
        this.dataColor = dataColor;
    }

    public int getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(int messageColor) {
        this.messageColor = messageColor;
    }

    public int getDataBackground() {
        return dataBackground;
    }

    public void setDataBackground(int dataBackground) {
        this.dataBackground = dataBackground;
    }

    public String getDataDrawable() {
        return dataDrawable;
    }

    public void setDataDrawable(String dataDrawable) {
        this.dataDrawable = dataDrawable;
    }
}
