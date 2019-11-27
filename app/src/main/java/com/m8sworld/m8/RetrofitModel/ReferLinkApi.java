package com.m8sworld.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferLinkApi {

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

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("business_name")
        @Expose
        private String businessName;
        @SerializedName("business_owner_name")
        @Expose
        private String businessOwnerName;
        @SerializedName("business_email")
        @Expose
        private String businessEmail;
        @SerializedName("business_phone")
        @Expose
        private String businessPhone;
        @SerializedName("your_name")
        @Expose
        private String yourName;
        @SerializedName("your_email")
        @Expose
        private String yourEmail;
        @SerializedName("your_phone")
        @Expose
        private String yourPhone;
        @SerializedName("share_app_url")
        @Expose
        private String shareAppUrl;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getBusinessOwnerName() {
            return businessOwnerName;
        }

        public void setBusinessOwnerName(String businessOwnerName) {
            this.businessOwnerName = businessOwnerName;
        }

        public String getBusinessEmail() {
            return businessEmail;
        }

        public void setBusinessEmail(String businessEmail) {
            this.businessEmail = businessEmail;
        }

        public String getBusinessPhone() {
            return businessPhone;
        }

        public void setBusinessPhone(String businessPhone) {
            this.businessPhone = businessPhone;
        }

        public String getYourName() {
            return yourName;
        }

        public void setYourName(String yourName) {
            this.yourName = yourName;
        }

        public String getYourEmail() {
            return yourEmail;
        }

        public void setYourEmail(String yourEmail) {
            this.yourEmail = yourEmail;
        }

        public String getYourPhone() {
            return yourPhone;
        }

        public void setYourPhone(String yourPhone) {
            this.yourPhone = yourPhone;
        }

        public String getShareAppUrl() {
            return shareAppUrl;
        }

        public void setShareAppUrl(String shareAppUrl) {
            this.shareAppUrl = shareAppUrl;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }
}
