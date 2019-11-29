package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DescriptedApi {

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


    public class Image {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }


    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("property_name")
        @Expose
        private Object propertyName;
        @SerializedName("property_type")
        @Expose
        private String propertyType;
        @SerializedName("property_sub_type")
        @Expose
        private String propertySubType;
        @SerializedName("property_slug")
        @Expose
        private Object propertySlug;
        @SerializedName("address_1")
        @Expose
        private String address1;
        @SerializedName("address_2")
        @Expose
        private String address2;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("county")
        @Expose
        private String county;
        @SerializedName("town")
        @Expose
        private String town;
        @SerializedName("postcode")
        @Expose
        private String postcode;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("rooms")
        @Expose
        private String rooms;
        @SerializedName("particularities")
        @Expose
        private String particularities;
        @SerializedName("minm")
        @Expose
        private String minm;
        @SerializedName("mls_id")
        @Expose
        private String mlsId;
        @SerializedName("floor")
        @Expose
        private String floor;
        @SerializedName("available_type")
        @Expose
        private String availableType;
        @SerializedName("added_since")
        @Expose
        private String addedSince;
        @SerializedName("real_estate_category")
        @Expose
        private String realEstateCategory;
        @SerializedName("property_description")
        @Expose
        private String propertyDescription;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("price_type")
        @Expose
        private Object priceType;
        @SerializedName("price_value")
        @Expose
        private Object priceValue;
        @SerializedName("sign_image")
        @Expose
        private Object signImage;
        @SerializedName("mandate_date")
        @Expose
        private Object mandateDate;
        @SerializedName("share_url_id")
        @Expose
        private Integer shareId;
        @SerializedName("images")
        @Expose
        private List<Image> images = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Object getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(Object propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        public String getPropertySubType() {
            return propertySubType;
        }

        public void setPropertySubType(String propertySubType) {
            this.propertySubType = propertySubType;
        }

        public Object getPropertySlug() {
            return propertySlug;
        }

        public void setPropertySlug(Object propertySlug) {
            this.propertySlug = propertySlug;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRooms() {
            return rooms;
        }

        public void setRooms(String rooms) {
            this.rooms = rooms;
        }

        public String getParticularities() {
            return particularities;
        }

        public void setParticularities(String particularities) {
            this.particularities = particularities;
        }

        public String getMinm() {
            return minm;
        }

        public void setMinm(String minm) {
            this.minm = minm;
        }

        public String getMlsId() {
            return mlsId;
        }

        public void setMlsId(String mlsId) {
            this.mlsId = mlsId;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getAvailableType() {
            return availableType;
        }

        public void setAvailableType(String availableType) {
            this.availableType = availableType;
        }

        public String getAddedSince() {
            return addedSince;
        }

        public void setAddedSince(String addedSince) {
            this.addedSince = addedSince;
        }

        public String getRealEstateCategory() {
            return realEstateCategory;
        }

        public void setRealEstateCategory(String realEstateCategory) {
            this.realEstateCategory = realEstateCategory;
        }

        public String getPropertyDescription() {
            return propertyDescription;
        }

        public void setPropertyDescription(String propertyDescription) {
            this.propertyDescription = propertyDescription;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public Object getPriceType() {
            return priceType;
        }

        public void setPriceType(Object priceType) {
            this.priceType = priceType;
        }

        public Object getPriceValue() {
            return priceValue;
        }

        public void setPriceValue(Object priceValue) {
            this.priceValue = priceValue;
        }

        public Object getSignImage() {
            return signImage;
        }

        public void setSignImage(Object signImage) {
            this.signImage = signImage;
        }

        public Object getMandateDate() {
            return mandateDate;
        }

        public void setMandateDate(Object mandateDate) {
            this.mandateDate = mandateDate;
        }

        public Integer getShareId() {
            return shareId;
        }

        public void setShareId(Integer shareId) {
            this.shareId = shareId;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

    }
}
