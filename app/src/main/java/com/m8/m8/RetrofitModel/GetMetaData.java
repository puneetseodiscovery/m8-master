package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMetaData {
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


    public class Profile {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("profile_type")
        @Expose
        private String profileType;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("contact_name")
        @Expose
        private String contactName;
        @SerializedName("b_name")
        @Expose
        private String bName;
        @SerializedName("b_phone")
        @Expose
        private String bPhone;
        @SerializedName("b_address")
        @Expose
        private String bAddress;
        @SerializedName("b_address2")
        @Expose
        private String bAddress2;
        @SerializedName("b_postalcode")
        @Expose
        private String bPostalcode;
        @SerializedName("b_town")
        @Expose
        private String bTown;
        @SerializedName("b_email")
        @Expose
        private String bEmail;
        @SerializedName("b_contact_name")
        @Expose
        private String bContactName;
        @SerializedName("b_country")
        @Expose
        private String bCountry;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("address_2")
        @Expose
        private String address2;
        @SerializedName("town")
        @Expose
        private String town;
        @SerializedName("postal_code")
        @Expose
        private String postalCode;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("leagal_name")
        @Expose
        private String leagalName;
        @SerializedName("leagal_email")
        @Expose
        private String leagalEmail;
        @SerializedName("leagal_phone")
        @Expose
        private String leagalPhone;
        @SerializedName("leagal_address")
        @Expose
        private String leagalAddress;
        @SerializedName("leagal_address2")
        @Expose
        private String leagalAddress2;
        @SerializedName("leagal_town")
        @Expose
        private String leagalTown;
        @SerializedName("leagal_postalcode")
        @Expose
        private String leagalPostalcode;
        @SerializedName("leagal_country")
        @Expose
        private String leagalCountry;
        @SerializedName("logo")
        @Expose
        private Object logo;
        @SerializedName("port_file")
        @Expose
        private Object portFile;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getProfileType() {
            return profileType;
        }

        public void setProfileType(String profileType) {
            this.profileType = profileType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getBName() {
            return bName;
        }

        public void setBName(String bName) {
            this.bName = bName;
        }

        public String getBPhone() {
            return bPhone;
        }

        public void setBPhone(String bPhone) {
            this.bPhone = bPhone;
        }

        public String getBAddress() {
            return bAddress;
        }

        public void setBAddress(String bAddress) {
            this.bAddress = bAddress;
        }

        public String getBAddress2() {
            return bAddress2;
        }

        public void setBAddress2(String bAddress2) {
            this.bAddress2 = bAddress2;
        }

        public String getBPostalcode() {
            return bPostalcode;
        }

        public void setBPostalcode(String bPostalcode) {
            this.bPostalcode = bPostalcode;
        }

        public String getBTown() {
            return bTown;
        }

        public void setBTown(String bTown) {
            this.bTown = bTown;
        }

        public String getBEmail() {
            return bEmail;
        }

        public void setBEmail(String bEmail) {
            this.bEmail = bEmail;
        }

        public String getBContactName() {
            return bContactName;
        }

        public void setBContactName(String bContactName) {
            this.bContactName = bContactName;
        }

        public String getBCountry() {
            return bCountry;
        }

        public void setBCountry(String bCountry) {
            this.bCountry = bCountry;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLeagalName() {
            return leagalName;
        }

        public void setLeagalName(String leagalName) {
            this.leagalName = leagalName;
        }

        public String getLeagalEmail() {
            return leagalEmail;
        }

        public void setLeagalEmail(String leagalEmail) {
            this.leagalEmail = leagalEmail;
        }

        public String getLeagalPhone() {
            return leagalPhone;
        }

        public void setLeagalPhone(String leagalPhone) {
            this.leagalPhone = leagalPhone;
        }

        public String getLeagalAddress() {
            return leagalAddress;
        }

        public void setLeagalAddress(String leagalAddress) {
            this.leagalAddress = leagalAddress;
        }

        public String getLeagalAddress2() {
            return leagalAddress2;
        }

        public void setLeagalAddress2(String leagalAddress2) {
            this.leagalAddress2 = leagalAddress2;
        }

        public String getLeagalTown() {
            return leagalTown;
        }

        public void setLeagalTown(String leagalTown) {
            this.leagalTown = leagalTown;
        }

        public String getLeagalPostalcode() {
            return leagalPostalcode;
        }

        public void setLeagalPostalcode(String leagalPostalcode) {
            this.leagalPostalcode = leagalPostalcode;
        }

        public String getLeagalCountry() {
            return leagalCountry;
        }

        public void setLeagalCountry(String leagalCountry) {
            this.leagalCountry = leagalCountry;
        }

        public Object getLogo() {
            return logo;
        }

        public void setLogo(Object logo) {
            this.logo = logo;
        }

        public Object getPortFile() {
            return portFile;
        }

        public void setPortFile(Object portFile) {
            this.portFile = portFile;
        }

    }

    public class Data {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("user_status")
        @Expose
        private String userStatus;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("email_verified_at")
        @Expose
        private Object emailVerifiedAt;
        @SerializedName("refer_code")
        @Expose
        private String referCode;
        @SerializedName("refer_by")
        @Expose
        private Object referBy;
        @SerializedName("contact_no")
        @Expose
        private String contactNo;
        @SerializedName("share_limit")
        @Expose
        private Integer shareLimit;
        @SerializedName("item_limit")
        @Expose
        private Integer itemLimit;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("plan_name")
        @Expose
        private String planName;
        @SerializedName("plan_end_date")
        @Expose
        private String planEndDate;
        @SerializedName("refer_earn")
        @Expose
        private Object referEarn;
        @SerializedName("profile")
        @Expose
        private Profile profile;

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

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getEmailVerifiedAt() {
            return emailVerifiedAt;
        }

        public void setEmailVerifiedAt(Object emailVerifiedAt) {
            this.emailVerifiedAt = emailVerifiedAt;
        }

        public String getReferCode() {
            return referCode;
        }

        public void setReferCode(String referCode) {
            this.referCode = referCode;
        }

        public Object getReferBy() {
            return referBy;
        }

        public void setReferBy(Object referBy) {
            this.referBy = referBy;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public Integer getShareLimit() {
            return shareLimit;
        }

        public void setShareLimit(Integer shareLimit) {
            this.shareLimit = shareLimit;
        }

        public Integer getItemLimit() {
            return itemLimit;
        }

        public void setItemLimit(Integer itemLimit) {
            this.itemLimit = itemLimit;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPlanEndDate() {
            return planEndDate;
        }

        public void setPlanEndDate(String planEndDate) {
            this.planEndDate = planEndDate;
        }

        public Object getReferEarn() {
            return referEarn;
        }

        public void setReferEarn(Object referEarn) {
            this.referEarn = referEarn;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }
    }

}
