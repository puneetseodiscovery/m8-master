package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetStartCategoryApi {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("show_itservice")
    @Expose
    private Boolean showItservice;
    @SerializedName("stripe")
    @Expose
    private Boolean stripe;
    @SerializedName("paypal")
    @Expose
    private Boolean paypal;

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

    public Boolean getShowItservice() {
        return showItservice;
    }

    public void setShowItservice(Boolean showItservice) {
        this.showItservice = showItservice;
    }

    public Boolean getStripe() {
        return stripe;
    }

    public void setStripe(Boolean stripe) {
        this.stripe = stripe;
    }

    public Boolean getPaypal() {
        return paypal;
    }

    public void setPaypal(Boolean paypal) {
        this.paypal = paypal;
    }


    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }
}
