package com.dukeai.manageloads.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentDetailsModel {

    @SerializedName("event_time")
    @Expose
    private String eventTime;
    @SerializedName("annotator_id")
    @Expose
    private String annotatorId;
    @SerializedName("sha1")
    @Expose
    private String sha1;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("inQB")
    @Expose
    private Boolean inQB;
    @SerializedName("doc_status")
    @Expose
    private String docStatus;
    @SerializedName("bucket")
    @Expose
    private String bucket;
    @SerializedName("uploadFrom")
    @Expose
    private String uploadFrom;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("get_doc_time")
    @Expose
    private String getDocTime;
    @SerializedName("inRDS")
    @Expose
    private Boolean inRDS;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("num_pages")
    @Expose
    private Double numPages;
    @SerializedName("uploaded")
    @Expose
    private String uploaded;
    @SerializedName("cust_id")
    @Expose
    private String custId;
    @SerializedName("extracts")
    @Expose
    private Extracts extracts;
    @SerializedName("send_doc_time")
    @Expose
    private String sendDocTime;

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getAnnotatorId() {
        return annotatorId;
    }

    public void setAnnotatorId(String annotatorId) {
        this.annotatorId = annotatorId;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Boolean getInQB() {
        return inQB;
    }

    public void setInQB(Boolean inQB) {
        this.inQB = inQB;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getUploadFrom() {
        return uploadFrom;
    }

    public void setUploadFrom(String uploadFrom) {
        this.uploadFrom = uploadFrom;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGetDocTime() {
        return getDocTime;
    }

    public void setGetDocTime(String getDocTime) {
        this.getDocTime = getDocTime;
    }

    public Boolean getInRDS() {
        return inRDS;
    }

    public void setInRDS(Boolean inRDS) {
        this.inRDS = inRDS;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getNumPages() {
        return numPages;
    }

    public void setNumPages(Double numPages) {
        this.numPages = numPages;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Extracts getExtracts() {
        return extracts;
    }

    public void setExtracts(Extracts extracts) {
        this.extracts = extracts;
    }

    public String getSendDocTime() {
        return sendDocTime;
    }

    public void setSendDocTime(String sendDocTime) {
        this.sendDocTime = sendDocTime;
    }

}

class Extracts {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("newCompany")
    @Expose
    private String newCompany;
    @SerializedName("newCategoryDict")
    @Expose
    private String newCategoryDict;
    @SerializedName("annotator_id")
    @Expose
    private String annotatorId;
    @SerializedName("doc_type")
    @Expose
    private String docType;
    @SerializedName("totals")
    @Expose
    private Totals totals;
//    @SerializedName("newMaincategoryDict_income")
//    @Expose
//    private String newMaincategoryDictIncome;
//    @SerializedName("newMaincategoryDict")
//    @Expose
//    private String newMaincategoryDict;
    @SerializedName("newCategoryAsset")
    @Expose
    private String newCategoryAsset;
    @SerializedName("duplicatedCheck")
    @Expose
    private String duplicatedCheck;
    @SerializedName("newCategory")
    @Expose
    private String newCategory;
    @SerializedName("get_doc_time")
    @Expose
    private String getDocTime;
    @SerializedName("imgIsShrink")
    @Expose
    private String imgIsShrink;
    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("send_doc_time")
    @Expose
    private String sendDocTime;
    @SerializedName("fileType")
    @Expose
    private String fileType;
//    @SerializedName("newMainCategoryCategory")
//    @Expose
//    private String newMainCategoryCategory;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getNewCompany() {
        return newCompany;
    }

    public void setNewCompany(String newCompany) {
        this.newCompany = newCompany;
    }

    public String getNewCategoryDict() {
        return newCategoryDict;
    }

    public void setNewCategoryDict(String newCategoryDict) {
        this.newCategoryDict = newCategoryDict;
    }

    public String getAnnotatorId() {
        return annotatorId;
    }

    public void setAnnotatorId(String annotatorId) {
        this.annotatorId = annotatorId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Totals getTotals() {
        return totals;
    }

    public void setTotals(Totals totals) {
        this.totals = totals;
    }

//    public String getNewMaincategoryDictIncome() {
//        return newMaincategoryDictIncome;
//    }
//
//    public void setNewMaincategoryDictIncome(String newMaincategoryDictIncome) {
//        this.newMaincategoryDictIncome = newMaincategoryDictIncome;
//    }
//
//    public String getNewMaincategoryDict() {
//        return newMaincategoryDict;
//    }
//
//    public void setNewMaincategoryDict(String newMaincategoryDict) {
//        this.newMaincategoryDict = newMaincategoryDict;
//    }

    public String getNewCategoryAsset() {
        return newCategoryAsset;
    }

    public void setNewCategoryAsset(String newCategoryAsset) {
        this.newCategoryAsset = newCategoryAsset;
    }

    public String getDuplicatedCheck() {
        return duplicatedCheck;
    }

    public void setDuplicatedCheck(String duplicatedCheck) {
        this.duplicatedCheck = duplicatedCheck;
    }

    public String getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(String newCategory) {
        this.newCategory = newCategory;
    }

    public String getGetDocTime() {
        return getDocTime;
    }

    public void setGetDocTime(String getDocTime) {
        this.getDocTime = getDocTime;
    }

    public String getImgIsShrink() {
        return imgIsShrink;
    }

    public void setImgIsShrink(String imgIsShrink) {
        this.imgIsShrink = imgIsShrink;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getSendDocTime() {
        return sendDocTime;
    }

    public void setSendDocTime(String sendDocTime) {
        this.sendDocTime = sendDocTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

//    public String getNewMainCategoryCategory() {
//        return newMainCategoryCategory;
//    }
//
//    public void setNewMainCategoryCategory(String newMainCategoryCategory) {
//        this.newMainCategoryCategory = newMainCategoryCategory;
//    }

}

class Datum {

    @SerializedName("Harbor Freight Tools_none")
    @Expose
    private HarborFreightToolsNone harborFreightToolsNone;
    @SerializedName("category_details")
    @Expose
    private CategoryDetails categoryDetails;

    public HarborFreightToolsNone getHarborFreightToolsNone() {
        return harborFreightToolsNone;
    }

    public void setHarborFreightToolsNone(HarborFreightToolsNone harborFreightToolsNone) {
        this.harborFreightToolsNone = harborFreightToolsNone;
    }

    public CategoryDetails getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(CategoryDetails categoryDetails) {
        this.categoryDetails = categoryDetails;
    }

}

class Totals {

    @SerializedName("net")
    @Expose
    private Double net;
    @SerializedName("loss")
    @Expose
    private Double loss;
    @SerializedName("revenue")
    @Expose
    private Double revenue;

    public Double getNet() {
        return net;
    }

    public void setNet(Double net) {
        this.net = net;
    }

    public Double getLoss() {
        return loss;
    }

    public void setLoss(Double loss) {
        this.loss = loss;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

}

class Header {

    @SerializedName("date")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

class HarborFreightToolsNone {

    @SerializedName("Company")
    @Expose
    private String company;
    @SerializedName("Receipt")
    @Expose
    private List<Receipt> receipt = null;
    @SerializedName("Invoice")
    @Expose
    private List<Object> invoice = null;
    @SerializedName("OrderID")
    @Expose
    private String orderID;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<Receipt> getReceipt() {
        return receipt;
    }

    public void setReceipt(List<Receipt> receipt) {
        this.receipt = receipt;
    }

    public List<Object> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<Object> invoice) {
        this.invoice = invoice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

}

class CategoryDetails {

    @SerializedName("Company")
    @Expose
    private String company;
    @SerializedName("Receipt")
    @Expose
    private List<Receipt__1> receipt = null;
    @SerializedName("Invoice")
    @Expose
    private List<Object> invoice = null;
    @SerializedName("OrderID")
    @Expose
    private String orderID;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<Receipt__1> getReceipt() {
        return receipt;
    }

    public void setReceipt(List<Receipt__1> receipt) {
        this.receipt = receipt;
    }

    public List<Object> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<Object> invoice) {
        this.invoice = invoice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

}

class Receipt {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("transactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("MainCategory")
    @Expose
    private String mainCategory;
    @SerializedName("assetType")
    @Expose
    private String assetType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

}

class Receipt__1 {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("transactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("MainCategory")
    @Expose
    private String mainCategory;
    @SerializedName("assetType")
    @Expose
    private String assetType;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

}