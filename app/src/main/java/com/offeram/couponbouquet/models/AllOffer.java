package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 03-Apr-18.
 */

public class AllOffer {
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("company_banner_image")
    @Expose
    private String companyBannerImage;
    @SerializedName("coupon_title")
    @Expose
    private String couponTitle;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("no_of_outlets")
    @Expose
    private String noOfOutlets;
    @SerializedName("outlets")
    @Expose
    private List<OutletForOffer> outlets = null;
    @SerializedName("photos")
    @Expose
    private List<CouponPhoto> photos = null;
    @SerializedName("user_coupons")
    @Expose
    private Integer userCoupons;
    @SerializedName("average_ratings")
    @Expose
    private String averageRatings;
    @SerializedName("is_starred")
    @Expose
    private Integer isStarred;
    @SerializedName("num_of_redeem")
    @Expose
    private Integer numOfRedeem;
    @SerializedName("cuisines")
    @Expose
    private String cuisines;
    @SerializedName("offer_text")
    @Expose
    private String offerText;

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

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyBannerImage() {
        return companyBannerImage;
    }

    public void setCompanyBannerImage(String companyBannerImage) {
        this.companyBannerImage = companyBannerImage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNoOfOutlets() {
        return noOfOutlets;
    }

    public void setNoOfOutlets(String noOfOutlets) {
        this.noOfOutlets = noOfOutlets;
    }

    public List<CouponPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<CouponPhoto> photos) {
        this.photos = photos;
    }

    public Integer getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(Integer userCoupons) {
        this.userCoupons = userCoupons;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public List<OutletForOffer> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<OutletForOffer> outlets) {
        this.outlets = outlets;
    }

    public String getAverageRatings() {
        return averageRatings;
    }

    public void setAverageRatings(String averageRatings) {
        this.averageRatings = averageRatings;
    }

    public Integer getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(Integer isStarred) {
        this.isStarred = isStarred;
    }

    public Integer getNumOfRedeem() {
        return numOfRedeem;
    }

    public void setNumOfRedeem(Integer numOfRedeem) {
        this.numOfRedeem = numOfRedeem;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }
}
