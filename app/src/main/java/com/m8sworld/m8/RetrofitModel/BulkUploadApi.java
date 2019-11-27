package com.m8sworld.m8.RetrofitModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulkUploadApi {
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

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("import_items")
        @Expose
        private Integer importItems;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("import_file")
        @Expose
        private String importFile;
        @SerializedName("total_items")
        @Expose
        private Integer totalItems;
        @SerializedName("logo")
        @Expose
        private String logo;
        @SerializedName("format")
        @Expose
        private String format;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("package")
        @Expose
        private Boolean _package;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public Integer getImportItems() {
            return importItems;
        }

        public void setImportItems(Integer importItems) {
            this.importItems = importItems;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImportFile() {
            return importFile;
        }

        public void setImportFile(String importFile) {
            this.importFile = importFile;
        }

        public Integer getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Integer totalItems) {
            this.totalItems = totalItems;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public Boolean getPackage() {
            return _package;
        }

        public void setPackage(Boolean _package) {
            this._package = _package;
        }

    }
}
