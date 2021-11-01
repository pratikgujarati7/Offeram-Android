package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.AllVersion;
import com.offeram.couponbouquet.models.PurchasedVersion;

import java.util.List;

/**
 * Created by admin on 15-Sep-17.
 */

public class PromoDiscount {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("discount_value")
    @Expose
    private String discountValue;
    @SerializedName("code_description")
    @Expose
    private String codeDescription;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("version_name")
    @Expose
    private String versionName;
    @SerializedName("version_side_menu_text")
    @Expose
    private String sideMenuText;
    @SerializedName("user_purchased_versions")
    @Expose
    private List<PurchasedVersion> purchasedVersions = null;
    @SerializedName("all_versions")
    @Expose
    private List<AllVersion> allVersions = null;

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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public List<PurchasedVersion> getPurchasedVersions() {
        return purchasedVersions;
    }

    public void setPurchasedVersions(List<PurchasedVersion> purchasedVersions) {
        this.purchasedVersions = purchasedVersions;
    }

    public List<AllVersion> getAllVersions() {
        return allVersions;
    }

    public void setAllVersions(List<AllVersion> allVersions) {
        this.allVersions = allVersions;
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

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }
}
