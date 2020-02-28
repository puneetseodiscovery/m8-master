package com.m8.m8.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommissionModel {

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

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("item_link")
        @Expose
        private String itemLink;
        @SerializedName("commission_earned")
        @Expose
        private String commissionEarned;
        @SerializedName("date_commission_paid")
        @Expose
        private String dateCommissionPaid;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getItemLink() {
            return itemLink;
        }

        public void setItemLink(String itemLink) {
            this.itemLink = itemLink;
        }

        public String getCommissionEarned() {
            return commissionEarned;
        }

        public void setCommissionEarned(String commissionEarned) {
            this.commissionEarned = commissionEarned;
        }

        public String getDateCommissionPaid() {
            return dateCommissionPaid;
        }

        public void setDateCommissionPaid(String dateCommissionPaid) {
            this.dateCommissionPaid = dateCommissionPaid;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

    }
}
