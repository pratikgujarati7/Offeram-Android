package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 14-Sep-17.
 */

public class PurchasedVersion {
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
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
    @SerializedName("payment_amount")
    @Expose
    private String paymentAmount;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;
    @SerializedName("version_side_menu_text")
    @Expose
    private String versionSideMenuText;
    @SerializedName("version_expiry_date")
    @Expose
    private String versionExpiryDate;
    @SerializedName("version_is_expired")
    @Expose
    private Integer versionIsExpired;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getVersionExpiryDate() {
        return versionExpiryDate;
    }

    public void setVersionExpiryDate(String versionExpiryDate) {
        this.versionExpiryDate = versionExpiryDate;
    }

    public String getVersionSideMenuText() {
        return versionSideMenuText;
    }

    public void setVersionSideMenuText(String versionSideMenuText) {
        this.versionSideMenuText = versionSideMenuText;
    }

    public Integer getVersionIsExpired() {
        return versionIsExpired;
    }

    public void setVersionIsExpired(Integer versionIsExpired) {
        this.versionIsExpired = versionIsExpired;
    }
}
