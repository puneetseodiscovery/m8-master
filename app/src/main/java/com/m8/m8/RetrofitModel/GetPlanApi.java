package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPlanApi {
    @SerializedName("status")
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


    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("plan_name")
        @Expose
        private String planName;
        @SerializedName("plan_share_qty")
        @Expose
        private String planShareQty;
        @SerializedName("plan_item_qty")
        @Expose
        private Integer planItemQty;
        @SerializedName("plan_price")
        @Expose
        private String planPrice;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("extra_details")
        @Expose
        private String extraDetails;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPlanShareQty() {
            return planShareQty;
        }

        public void setPlanShareQty(String planShareQty) {
            this.planShareQty = planShareQty;
        }

        public Integer getPlanItemQty() {
            return planItemQty;
        }

        public void setPlanItemQty(Integer planItemQty) {
            this.planItemQty = planItemQty;
        }

        public String getPlanPrice() {
            return planPrice;
        }

        public void setPlanPrice(String planPrice) {
            this.planPrice = planPrice;
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

        public String getExtraDetails() {
            return extraDetails;
        }

        public void setExtraDetails(String extraDetails) {
            this.extraDetails = extraDetails;
        }

    }
}
