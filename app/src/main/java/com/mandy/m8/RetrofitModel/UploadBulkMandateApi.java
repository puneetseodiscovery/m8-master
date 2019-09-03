package com.mandy.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadBulkMandateApi {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("import_file")
        @Expose
        private String importFile;
        @SerializedName("logo")
        @Expose
        private Object logo;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("total_items")
        @Expose
        private Integer totalItems;
        @SerializedName("import_items")
        @Expose
        private Integer importItems;
        @SerializedName("mandate_date")
        @Expose
        private String mandateDate;
        @SerializedName("sign_image")
        @Expose
        private String signImage;
        @SerializedName("mandate_pdf")
        @Expose
        private String mandatePdf;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("total_commision")
        @Expose
        private String totalCommision;
        @SerializedName("price_type")
        @Expose
        private String priceType;
        @SerializedName("price_value")
        @Expose
        private String priceValue;
        @SerializedName("step")
        @Expose
        private Integer step;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getImportFile() {
            return importFile;
        }

        public void setImportFile(String importFile) {
            this.importFile = importFile;
        }

        public Object getLogo() {
            return logo;
        }

        public void setLogo(Object logo) {
            this.logo = logo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Integer totalItems) {
            this.totalItems = totalItems;
        }

        public Integer getImportItems() {
            return importItems;
        }

        public void setImportItems(Integer importItems) {
            this.importItems = importItems;
        }

        public String getMandateDate() {
            return mandateDate;
        }

        public void setMandateDate(String mandateDate) {
            this.mandateDate = mandateDate;
        }

        public String getSignImage() {
            return signImage;
        }

        public void setSignImage(String signImage) {
            this.signImage = signImage;
        }

        public String getMandatePdf() {
            return mandatePdf;
        }

        public void setMandatePdf(String mandatePdf) {
            this.mandatePdf = mandatePdf;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getTotalCommision() {
            return totalCommision;
        }

        public void setTotalCommision(String totalCommision) {
            this.totalCommision = totalCommision;
        }

        public String getPriceType() {
            return priceType;
        }

        public void setPriceType(String priceType) {
            this.priceType = priceType;
        }

        public String getPriceValue() {
            return priceValue;
        }

        public void setPriceValue(String priceValue) {
            this.priceValue = priceValue;
        }

        public Integer getStep() {
            return step;
        }

        public void setStep(Integer step) {
            this.step = step;
        }

    }
}
