package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponPhoto {
    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("coupon_image")
    @Expose
    private String couponImage;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
    }
}
