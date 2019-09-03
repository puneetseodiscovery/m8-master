package com.mandy.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComissionListApi {
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Image {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("images")
        @Expose
        private List<Image> images = null;
        @SerializedName("mandate")
        @Expose
        private Mandate mandate;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public Mandate getMandate() {
            return mandate;
        }

        public void setMandate(Mandate mandate) {
            this.mandate = mandate;
        }

    }

    public class Mandate {

        @SerializedName("mandate_id")
        @Expose
        private Integer mandateId;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("price_type")
        @Expose
        private String priceType;
        @SerializedName("price_value")
        @Expose
        private String priceValue;
        @SerializedName("calculate_price")
        @Expose
        private String calculatePrice;
        @SerializedName("mandate_date")
        @Expose
        private String mandateDate;
        @SerializedName("sign_image")
        @Expose
        private String signImage;
        @SerializedName("mandate_pdf")
        @Expose
        private String mandatePdf;
        @SerializedName("agree")
        @Expose
        private Integer agree;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getMandateId() {
            return mandateId;
        }

        public void setMandateId(Integer mandateId) {
            this.mandateId = mandateId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
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

        public String getCalculatePrice() {
            return calculatePrice;
        }

        public void setCalculatePrice(String calculatePrice) {
            this.calculatePrice = calculatePrice;
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

        public Integer getAgree() {
            return agree;
        }

        public void setAgree(Integer agree) {
            this.agree = agree;
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

    }
}
