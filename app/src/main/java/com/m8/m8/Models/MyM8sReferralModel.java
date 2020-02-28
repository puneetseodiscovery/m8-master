package com.m8.m8.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyM8sReferralModel {
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
        @SerializedName("refereed_name")
        @Expose
        private String refereedName;
        @SerializedName("refferal")
        @Expose
        private String refferal;
        @SerializedName("package_purchased")
        @Expose
        private String packagePurchased;
        @SerializedName("date_paid")
        @Expose
        private String datePaid;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRefereedName() {
            return refereedName;
        }

        public void setRefereedName(String refereedName) {
            this.refereedName = refereedName;
        }

        public String getRefferal() {
            return refferal;
        }

        public void setRefferal(String refferal) {
            this.refferal = refferal;
        }

        public String getPackagePurchased() {
            return packagePurchased;
        }

        public void setPackagePurchased(String packagePurchased) {
            this.packagePurchased = packagePurchased;
        }

        public String getDatePaid() {
            return datePaid;
        }

        public void setDatePaid(String datePaid) {
            this.datePaid = datePaid;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

    }
}
