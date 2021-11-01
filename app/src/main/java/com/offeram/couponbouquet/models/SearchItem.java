package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 14-Apr-18.
 */

public class SearchItem {
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
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
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("company_banner_image")
    @Expose
    private String companyBannerImage;
    @SerializedName("is_favorite")
    @Expose
    private String isFavorite;
    @SerializedName("is_used")
    @Expose
    private Integer isUsed;
    @SerializedName("num_of_redeem")
    @Expose
    private Integer numOfRedeem;
    @SerializedName("ratings")
    @Expose
    private List<Rating> ratings = null;
    @SerializedName("average_ratings")
    @Expose
    private String averageRatings;
    @SerializedName("total_rating")
    @Expose
    private String totalRating;
    @SerializedName("outlets")
    @Expose
    private List<OutletForOffer> outlets = null;
    @SerializedName("is_expired")
    @Expose
    private Integer isExpired;
    @SerializedName("coupon_at_available_outlets")
    @Expose
    private List<CouponOutlet> couponAtAvailableOutlets = null;
    @SerializedName("num_of_offers")
    @Expose
    private Integer numOfOffers;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("ping_status")
    @Expose
    private String pingStatus;
    @SerializedName("ping_from_name")
    @Expose
    private String pingFromName;
    @SerializedName("ping_id")
    @Expose
    private String pingId;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
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

    public String getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
    }

    public String getCompanyBannerImage() {
        return companyBannerImage;
    }

    public void setCompanyBannerImage(String companyBannerImage) {
        this.companyBannerImage = companyBannerImage;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getNumOfRedeem() {
        return numOfRedeem;
    }

    public void setNumOfRedeem(Integer numOfRedeem) {
        this.numOfRedeem = numOfRedeem;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(String averageRatings) {
        this.averageRatings = averageRatings;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public List<OutletForOffer> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<OutletForOffer> outlets) {
        this.outlets = outlets;
    }

    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public List<CouponOutlet> getCouponAtAvailableOutlets() {
        return couponAtAvailableOutlets;
    }

    public void setCouponAtAvailableOutlets(List<CouponOutlet> couponAtAvailableOutlets) {
        this.couponAtAvailableOutlets = couponAtAvailableOutlets;
    }

    public Integer getNumOfOffers() {
        return numOfOffers;
    }

    public void setNumOfOffers(Integer numOfOffers) {
        this.numOfOffers = numOfOffers;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    public String getPingFromName() {
        return pingFromName;
    }

    public void setPingFromName(String pingFromName) {
        this.pingFromName = pingFromName;
    }

    public String getPingId() {
        return pingId;
    }

    public void setPingId(String pingId) {
        this.pingId = pingId;
    }
}
