package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddMandataApi {
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
        private String itemId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("price_type")
        @Expose
        private Object priceType;
        @SerializedName("price_value")
        @Expose
        private Object priceValue;
        @SerializedName("meight_commision")
        @Expose
        private Object meightCommision;
        @SerializedName("agent_commission")
        @Expose
        private Object agentCommission;
        @SerializedName("total_commision_amount")
        @Expose
        private Double totalCommisionAmount;
        @SerializedName("calculate_price")
        @Expose
        private Object calculatePrice;
        @SerializedName("total_commision")
        @Expose
        private String totalCommision;
        @SerializedName("mandate_date")
        @Expose
        private Object mandateDate;
        @SerializedName("sign_image")
        @Expose
        private Object signImage;
        @SerializedName("mandate_pdf")
        @Expose
        private Object mandatePdf;
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
        @SerializedName("m8_commission")
        @Expose
        private String m8Commission;
        @SerializedName("fixed_start")
        @Expose
        private Integer fixedStart;
        @SerializedName("fixed_end")
        @Expose
        private Integer fixedEnd;

        public Integer getMandateId() {
            return mandateId;
        }

        public void setMandateId(Integer mandateId) {
            this.mandateId = mandateId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
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

        public Object getMeightCommision() {
            return meightCommision;
        }

        public void setMeightCommision(Object meightCommision) {
            this.meightCommision = meightCommision;
        }

        public Object getAgentCommission() {
            return agentCommission;
        }

        public void setAgentCommission(Object agentCommission) {
            this.agentCommission = agentCommission;
        }

        public Double getTotalCommisionAmount() {
            return totalCommisionAmount;
        }

        public void setTotalCommisionAmount(Double totalCommisionAmount) {
            this.totalCommisionAmount = totalCommisionAmount;
        }

        public Object getCalculatePrice() {
            return calculatePrice;
        }

        public void setCalculatePrice(Object calculatePrice) {
            this.calculatePrice = calculatePrice;
        }

        public String getTotalCommision() {
            return totalCommision;
        }

        public void setTotalCommision(String totalCommision) {
            this.totalCommision = totalCommision;
        }

        public Object getMandateDate() {
            return mandateDate;
        }

        public void setMandateDate(Object mandateDate) {
            this.mandateDate = mandateDate;
        }

        public Object getSignImage() {
            return signImage;
        }

        public void setSignImage(Object signImage) {
            this.signImage = signImage;
        }

        public Object getMandatePdf() {
            return mandatePdf;
        }

        public void setMandatePdf(Object mandatePdf) {
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

        public String getM8Commission() {
            return m8Commission;
        }

        public void setM8Commission(String m8Commission) {
            this.m8Commission = m8Commission;
        }

        public Integer getFixedStart() {
            return fixedStart;
        }

        public void setFixedStart(Integer fixedStart) {
            this.fixedStart = fixedStart;
        }

        public Integer getFixedEnd() {
            return fixedEnd;
        }

        public void setFixedEnd(Integer fixedEnd) {
            this.fixedEnd = fixedEnd;
        }

    }
}