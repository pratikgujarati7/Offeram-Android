package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 14-Sep-17.
 */

public class AllVersion {
    @SerializedName("version_expiry_date")
    @Expose
    private String versionExpiryDate;
    @SerializedName("version_id")
    @Expose
    private String versionId;
    @SerializedName("version_name")
    @Expose
    private String versionName;
    @SerializedName("to_be_sold")
    @Expose
    private String toBeSold;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("version_price")
    @Expose
    private String versionPrice;
    @SerializedName("version_side_menu_text")
    @Expose
    private String versionSideMenuText;
    @SerializedName("promotional_code")
    @Expose
    private String promotionalCode;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;

    public String getVersionExpiryDate() {
        return versionExpiryDate;
    }

    public void setVersionExpiryDate(String versionExpiryDate) {
        this.versionExpiryDate = versionExpiryDate;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getToBeSold() {
        return toBeSold;
    }

    public void setToBeSold(String toBeSold) {
        this.toBeSold = toBeSold;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersionPrice() {
        return versionPrice;
    }

    public void setVersionPrice(String versionPrice) {
        this.versionPrice = versionPrice;
    }

    public String getPromotionalCode() {
        return promotionalCode;
    }

    public void setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getVersionSideMenuText() {
        return versionSideMenuText;
    }

    public void setVersionSideMenuText(String versionSideMenuText) {
        this.versionSideMenuText = versionSideMenuText;
    }
}
