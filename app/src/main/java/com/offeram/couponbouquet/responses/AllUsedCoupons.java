package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.UsedOffer;

import java.util.List;

/**
 * Created by admin on 14-Sep-17.
 */

public class AllUsedCoupons {
    @SerializedName("offeram_coin_balance")
    @Expose
    private String offeram_coin_balance;
    @SerializedName("user_used_coupons")
    @Expose
    private List<UsedOffer> userUsedCoupons = null;

    public String getOfferam_coin_balance() {
        return offeram_coin_balance;
    }

    public void setOfferam_coin_balance(String offeram_coin_balance) {
        this.offeram_coin_balance = offeram_coin_balance;
    }

    public List<UsedOffer> getUserUsedCoupons() {
        return userUsedCoupons;
    }

    public void setUserUsedCoupons(List<UsedOffer> userUsedCoupons) {
        this.userUsedCoupons = userUsedCoupons;
    }

}
