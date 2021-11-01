package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.AllVersion;
import com.offeram.couponbouquet.models.PurchasedVersion;

import java.util.List;

/**
 * Created by admin on 14-Sep-17.
 */

public class PurchaseVersion {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("user_purchased_versions")
    @Expose
    private List<PurchasedVersion> purchasedVersions = null;
    @SerializedName("all_versions")
    @Expose
    private List<AllVersion> allVersions = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("version_name")
    @Expose
    private String versionName;
    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;
    @SerializedName("version_side_menu_text")
    @Expose
    private String sideMenuText;

    public List<PurchasedVersion> getPurchasedVersions() {
        return purchasedVersions;
    }

    public void setPurchasedVersions(List<PurchasedVersion> userPurchasedVersions) {
        this.purchasedVersions = userPurchasedVersions;
    }

    public List<AllVersion> getAllVersions() {
        return allVersions;
    }

    public void setAllVersions(List<AllVersion> allVersions) {
        this.allVersions = allVersions;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }


    public String getSideMenuText() {
        return sideMenuText;
    }

    public void setSideMenuText(String sideMenuText) {
        this.sideMenuText = sideMenuText;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
