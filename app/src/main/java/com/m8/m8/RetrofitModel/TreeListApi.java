package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreeListApi {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("mycommision")
    @Expose
    private Mycommision mycommision;

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

    public Mycommision getMycommision() {
        return mycommision;
    }

    public void setMycommision(Mycommision mycommision) {
        this.mycommision = mycommision;
    }

    public class Mycommision {

        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("currency")
        @Expose
        private String currency;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }

    public class Treeuser {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_image")
        @Expose
        private Object profileImage;
        @SerializedName("isBuyer")
        @Expose
        private Boolean isBuyer;
        @SerializedName("parentusers")
        @Expose
        private Integer parentusers;

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

        public Object getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(Object profileImage) {
            this.profileImage = profileImage;
        }

        public Boolean getIsBuyer() {
            return isBuyer;
        }

        public void setIsBuyer(Boolean isBuyer) {
            this.isBuyer = isBuyer;
        }

        public Integer getParentusers() {
            return parentusers;
        }

        public void setParentusers(Integer parentusers) {
            this.parentusers = parentusers;
        }

    }

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("isBuyer")
        @Expose
        private Boolean isBuyer;
        @SerializedName("treeusers")
        @Expose
        private List<Treeuser> treeusers = null;

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

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public Boolean getIsBuyer() {
            return isBuyer;
        }

        public void setIsBuyer(Boolean isBuyer) {
            this.isBuyer = isBuyer;
        }

        public List<Treeuser> getTreeusers() {
            return treeusers;
        }

        public void setTreeusers(List<Treeuser> treeusers) {
            this.treeusers = treeusers;
        }

    }

}
