package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOtpApi {
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

        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("profile_status")
        @Expose
        private Integer profileStatus;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getProfileStatus() {
            return profileStatus;
        }

        public void setProfileStatus(Integer profileStatus) {
            this.profileStatus = profileStatus;
        }
    }
}
