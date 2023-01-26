package com.dukeai.manageloads.model;

public class ByteStreamResponseModel {
    public byte[] bytes;
    public String fileName;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
