package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class Merchant {

    @SerializedName("pinged_user_name")
    @Expose
    private String pinged_user_name;
    @SerializedName("from_user_name")
    @Expose
    private String from_user_name;
    @SerializedName("type")
    @Expose
    private String type;
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
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("ratings")
    @Expose
    private List<Rating> ratings = null;
    @SerializedName("average_ratings")
    @Expose
    private String averageRatings;
    @SerializedName("total_rating")
    @Expose
    private String totalRating;
    @SerializedName("timings")
    @Expose
    private String timings;
    @SerializedName("is_starred")
    @Expose
    private Integer isStarred;
    @SerializedName("is_pinged")
    @Expose
    private Integer isPinged;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("photos")
    @Expose
    private List<CouponPhoto> photos = null;
    @SerializedName("coupons_list")
    @Expose
    private List<Coupon> coupons = null;
    @SerializedName("user_coupons")
    @Expose
    private Integer userCoupons;
    @SerializedName("menu_photos")
    @Expose
    private List<Photo> menuPhotos = null;
    @SerializedName("infrastructure_photos")
    @Expose
    private List<Photo> infrastructurePhotos = null;
    @SerializedName("outlets") // Note :- For sorting outlets according to distance in MerchantDetailsActivity
    @Expose
    private List<OutletForOffer> outlets = null;

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getPinged_user_name() {
        return pinged_user_name;
    }

    public void setPinged_user_name(String pinged_user_name) {
        this.pinged_user_name = pinged_user_name;
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

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<Photo> getMenuPhotos() {
        return menuPhotos;
    }

    public void setMenuPhotos(List<Photo> menuPhotos) {
        this.menuPhotos = menuPhotos;
    }

    public List<Photo> getInfrastructurePhotos() {
        return infrastructurePhotos;
    }

    public void setInfrastructurePhotos(List<Photo> infrastructurePhotos) {
        this.infrastructurePhotos = infrastructurePhotos;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public Integer getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(Integer isStarred) {
        this.isStarred = isStarred;
    }

    public Integer getIsPinged() {
        return isPinged;
    }

    public void setIsPinged(Integer isPinged) {
        this.isPinged = isPinged;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OutletForOffer> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<OutletForOffer> outlets) {
        this.outlets = outlets;
    }
}
