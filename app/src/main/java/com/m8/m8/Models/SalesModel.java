package com.m8.m8.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesModel {

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
        @SerializedName("sales_price")
        @Expose
        private String salesPrice;
        @SerializedName("commission_offered")
        @Expose
        private String commissionOffered;
        @SerializedName("commission_earned")
        @Expose
        private String commissionEarned;
        @SerializedName("date_paid")
        @Expose
        private String datePaid;
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

        public String getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(String salesPrice) {
            this.salesPrice = salesPrice;
        }

        public String getCommissionOffered() {
            return commissionOffered;
        }

        public void setCommissionOffered(String commissionOffered) {
            this.commissionOffered = commissionOffered;
        }

        public String getCommissionEarned() {
            return commissionEarned;
        }

        public void setCommissionEarned(String commissionEarned) {
            this.commissionEarned = commissionEarned;
        }

        public String getDatePaid() {
            return datePaid;
        }

        public void setDatePaid(String datePaid) {
            this.datePaid = datePaid;
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
