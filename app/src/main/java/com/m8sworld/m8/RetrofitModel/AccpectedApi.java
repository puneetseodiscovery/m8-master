package com.m8sworld.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccpectedApi {

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

        @SerializedName("accepted_user_id")
        @Expose
        private String acceptedUserId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("share_url_id")
        @Expose
        private String shareUrlId;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getAcceptedUserId() {
            return acceptedUserId;
        }

        public void setAcceptedUserId(String acceptedUserId) {
            this.acceptedUserId = acceptedUserId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getShareUrlId() {
            return shareUrlId;
        }

        public void setShareUrlId(String shareUrlId) {
            this.shareUrlId = shareUrlId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }

}
