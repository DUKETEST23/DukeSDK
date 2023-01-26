package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipientDataModel implements Serializable {
    @SerializedName("company_name")
    private String companyName;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("contact")
    private String contact;

    @SerializedName("email")
    private String email;

    @SerializedName("isCustomRecipient")
    private String isCustomRecipient;

    public String getIsCustomRecipient() {
        return isCustomRecipient;
    }

    public void setIsCustomRecipient(String isCustomRecipient) {
        this.isCustomRecipient = isCustomRecipient;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
