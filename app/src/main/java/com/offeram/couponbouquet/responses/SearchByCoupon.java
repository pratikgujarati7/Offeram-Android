package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Coupon;

import java.util.List;

/**
 * Created by admin on 14-Sep-17.
 */

public class SearchByCoupon {
    @SerializedName("coupons_list")
    @Expose
    private List<Coupon> couponsList = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<Coupon> getSearchedCouponsList() {
        return couponsList;
    }

    public void setSearchedCouponsList(List<Coupon> couponsList) {
        this.couponsList = couponsList;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
