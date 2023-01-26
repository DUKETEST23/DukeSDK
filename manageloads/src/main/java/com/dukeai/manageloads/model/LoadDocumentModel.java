package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoadDocumentModel implements Serializable {

    @SerializedName("sha1")
    private String sha1;

    @SerializedName("filename")
    private String fileName;

    @SerializedName("doc_type")
    private String docType;

    @SerializedName("num_pages")
    private String totalPages;

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

   /* @SerializedName("sha1")
    @Expose
    private String sha1;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("doc_type")
    @Expose
    private Object docType;
    @SerializedName("num_pages")
    @Expose
    private Float numPages;
    @SerializedName("doc_title")
    @Expose
    private String docTitle;

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Object getDocType() {
        return docType;
    }

    public void setDocType(Object docType) {
        this.docType = docType;
    }

    public Float getNumPages() {
        return numPages;
    }

    public void setNumPages(Float numPages) {
        this.numPages = numPages;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }*/
}
