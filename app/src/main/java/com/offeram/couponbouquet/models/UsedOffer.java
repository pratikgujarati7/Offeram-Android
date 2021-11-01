package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 03-Apr-18.
 */

public class UsedOffer {
    @SerializedName("redemption_id")
    @Expose
    private String redemptionId;
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("date_used")
    @Expose
    private String dateUsed;
    @SerializedName("area_name")
    @Expose
    private String areaName;
    @SerializedName("coupon_title")
    @Expose
    private String couponTitle;
    @SerializedName("coupon_description")
    @Expose
    private String couponDescription;
    @SerializedName("terms_conditions")
    @Expose
    private String termsConditions;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("version_id")
    @Expose
    private String versionId;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("coupon_image")
    @Expose
    private String couponImage;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("average_ratings")
    @Expose
    private String averageRatings;

    @SerializedName("reuse_price")
    @Expose
    private String reuse_price;
    @SerializedName("is_reused")
    @Expose
    private String is_reused;
    @SerializedName("total_rating")
    @Expose
    private Integer totalRating;
    @SerializedName("ratings")
    @Expose
    private List<Rating> ratings = null;
    @SerializedName("is_starred")
    @Expose
    private Integer isStarred;

    public String getReuse_price() {
        return reuse_price;
    }

    public void setReuse_price(String reuse_price) {
        this.reuse_price = reuse_price;
    }

    public String getIs_reused() {
        return is_reused;
    }

    public void setIs_reused(String is_reused) {
        this.is_reused = is_reused;
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public String getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(String averageRatings) {
        this.averageRatings = averageRatings;
    }

    public Integer getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Integer totalRating) {
        this.totalRating = totalRating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Integer getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(Integer isStarred) {
        this.isStarred = isStarred;
    }
}
