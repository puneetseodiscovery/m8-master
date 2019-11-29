package com.m8.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDataApi {
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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("profile_type")
        @Expose
        private String profileType;
        @SerializedName("name")
        @Expose
        private String name;
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
        private String logo;
        @SerializedName("port_file")
        @Expose
        private String portFile;

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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getPortFile() {
            return portFile;
        }

        public void setPortFile(String portFile) {
            this.portFile = portFile;
        }

    }
}
