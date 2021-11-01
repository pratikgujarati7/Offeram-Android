package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coupon {
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("coupon_title")
    @Expose
    private String couponTitle;
    @SerializedName("coupon_description")
    @Expose
    private String couponDescription;
    @SerializedName("date_added")
    @Expose
    private String dateAdded;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("terms_conditions")
    @Expose
    private String termsConditions;
    @SerializedName("coupon_image")
    @Expose
    private String couponImage;
    @SerializedName("is_used")
    @Expose
    private String isUsed;
    @SerializedName("ping_status")
    @Expose
    private String ping_status;
    @SerializedName("ping_id")
    @Expose
    private String ping_id;
    @SerializedName("is_expired")
    @Expose
    private String isExpired;
    @SerializedName("redemption_id")
    @Expose
    private String redemptionId;
    @SerializedName("date_used")
    @Expose
    private String dateUsed;
    @SerializedName("area_name")
    @Expose
    private String areaName;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("version_id")
    @Expose
    private String versionId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("coupon_at_available_outlets")
    @Expose
    private List<CouponOutlet> couponAtAvailableOutlets = null;
    @SerializedName("num_of_redeem")
    @Expose
    private Integer numOfRedeem;
    @SerializedName("is_starred")
    @Expose
    private Integer isStarred;

    @SerializedName("is_pinged")
    @Expose
    private String is_pinged;

    public String getIs_pinged() {
        return is_pinged;
    }

    public void setIs_pinged(String is_pinged) {
        this.is_pinged = is_pinged;
    }

    public String getPing_id() {
        return ping_id;
    }

    public void setPing_id(String ping_id) {
        this.ping_id = ping_id;
    }

    public String getPing_status() {
        return ping_status;
    }

    public void setPing_status(String ping_status) {
        this.ping_status = ping_status;
    }

    public String getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(String redemptionId) {
        this.redemptionId = redemptionId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getDateUsed() {
        return dateUsed;
    }

    public void setDateUsed(String dateUsed) {
        this.dateUsed = dateUsed;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public List<CouponOutlet> getCouponAtAvailableOutlets() {
        return couponAtAvailableOutlets;
    }

    public void setCouponAtAvailableOutlets(List<CouponOutlet> couponAtAvailableOutlets) {
        this.couponAtAvailableOutlets = couponAtAvailableOutlets;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }

    public Integer getNumOfRedeem() {
        return numOfRedeem;
    }

    public void setNumOfRedeem(Integer numOfRedeem) {
        this.numOfRedeem = numOfRedeem;
    }

    public Integer getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(Integer isStarred) {
        this.isStarred = isStarred;
    }
}
