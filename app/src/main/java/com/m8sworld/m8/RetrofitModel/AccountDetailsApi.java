package com.m8sworld.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDetailsApi {
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

        @SerializedName("account_status")
        @Expose
        private String accountStatus;
        @SerializedName("property_listed")
        @Expose
        private Integer propertyListed;
        @SerializedName("commision_tree_in_play")
        @Expose
        private Integer commisionTreeInPlay;
        @SerializedName("viewing_through")
        @Expose
        private Integer viewingThrough;
        @SerializedName("sales_through")
        @Expose
        private Integer salesThrough;
        @SerializedName("my_mandates")
        @Expose
        private Integer myMandates;
        @SerializedName("earnings_to_date")
        @Expose
        private String earningsToDate;
        @SerializedName("paid_out")
        @Expose
        private String paidOut;
        @SerializedName("pay_outstanding_balance")
        @Expose
        private String payOutstandingBalance;
        @SerializedName("currency")
        @Expose
        private String currency;

        @SerializedName("refer_earn")
        @Expose
        private String referEarn;

        public String getReferEarn() {
            return referEarn;
        }

        public void setReferEarn(String referEarn) {
            this.referEarn = referEarn;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public Integer getPropertyListed() {
            return propertyListed;
        }

        public void setPropertyListed(Integer propertyListed) {
            this.propertyListed = propertyListed;
        }

        public Integer getCommisionTreeInPlay() {
            return commisionTreeInPlay;
        }

        public void setCommisionTreeInPlay(Integer commisionTreeInPlay) {
            this.commisionTreeInPlay = commisionTreeInPlay;
        }

        public Integer getViewingThrough() {
            return viewingThrough;
        }

        public void setViewingThrough(Integer viewingThrough) {
            this.viewingThrough = viewingThrough;
        }

        public Integer getSalesThrough() {
            return salesThrough;
        }

        public void setSalesThrough(Integer salesThrough) {
            this.salesThrough = salesThrough;
        }

        public Integer getMyMandates() {
            return myMandates;
        }

        public void setMyMandates(Integer myMandates) {
            this.myMandates = myMandates;
        }

        public String getEarningsToDate() {
            return earningsToDate;
        }

        public void setEarningsToDate(String earningsToDate) {
            this.earningsToDate = earningsToDate;
        }

        public String getPaidOut() {
            return paidOut;
        }

        public void setPaidOut(String paidOut) {
            this.paidOut = paidOut;
        }

        public String getPayOutstandingBalance() {
            return payOutstandingBalance;
        }

        public void setPayOutstandingBalance(String payOutstandingBalance) {
            this.payOutstandingBalance = payOutstandingBalance;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }
}
