package com.mandy.m8car.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermsConditionApi {
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

        @SerializedName("term_and_condition")
        @Expose
        private String termAndCondition;
        @SerializedName("privacy_policy")
        @Expose
        private String privacyPolicy;
        @SerializedName("art_statement")
        @Expose
        private String artStatement;

        public String getTermAndCondition() {
            return termAndCondition;
        }

        public void setTermAndCondition(String termAndCondition) {
            this.termAndCondition = termAndCondition;
        }

        public String getPrivacyPolicy() {
            return privacyPolicy;
        }

        public void setPrivacyPolicy(String privacyPolicy) {
            this.privacyPolicy = privacyPolicy;
        }

        public String getArtStatement() {
            return artStatement;
        }

        public void setArtStatement(String artStatement) {
            this.artStatement = artStatement;
        }

    }

}
