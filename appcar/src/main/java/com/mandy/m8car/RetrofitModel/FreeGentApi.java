package com.mandy.m8car.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreeGentApi {

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
        private Object priceType;
        @SerializedName("price_value")
        @Expose
        private Object priceValue;
        @SerializedName("meight_commision")
        @Expose
        private Double meightCommision;
        @SerializedName("agent_commission")
        @Expose
        private Double agentCommission;
        @SerializedName("total_commision_amount")
        @Expose
        private String totalCommisionAmount;
        @SerializedName("calculate_price")
        @Expose
        private Double calculatePrice;
        @SerializedName("total_commision")
        @Expose
        private String totalCommision;
        @SerializedName("mandate_date")
        @Expose
        private String mandateDate;
        @SerializedName("sign_image")
        @Expose
        private Object signImage;
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
        @SerializedName("step")
        @Expose
        private Integer step;

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

        public Object getPriceType() {
            return priceType;
        }

        public void setPriceType(Object priceType) {
            this.priceType = priceType;
        }

        public Object getPriceValue() {
            return priceValue;
        }

        public void setPriceValue(Object priceValue) {
            this.priceValue = priceValue;
        }

        public Double getMeightCommision() {
            return meightCommision;
        }

        public void setMeightCommision(Double meightCommision) {
            this.meightCommision = meightCommision;
        }

        public Double getAgentCommission() {
            return agentCommission;
        }

        public void setAgentCommission(Double agentCommission) {
            this.agentCommission = agentCommission;
        }

        public String getTotalCommisionAmount() {
            return totalCommisionAmount;
        }

        public void setTotalCommisionAmount(String totalCommisionAmount) {
            this.totalCommisionAmount = totalCommisionAmount;
        }

        public Double getCalculatePrice() {
            return calculatePrice;
        }

        public void setCalculatePrice(Double calculatePrice) {
            this.calculatePrice = calculatePrice;
        }

        public String getTotalCommision() {
            return totalCommision;
        }

        public void setTotalCommision(String totalCommision) {
            this.totalCommision = totalCommision;
        }

        public String getMandateDate() {
            return mandateDate;
        }

        public void setMandateDate(String mandateDate) {
            this.mandateDate = mandateDate;
        }

        public Object getSignImage() {
            return signImage;
        }

        public void setSignImage(Object signImage) {
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

        public Integer getStep() {
            return step;
        }

        public void setStep(Integer step) {
            this.step = step;
        }

    }

}
