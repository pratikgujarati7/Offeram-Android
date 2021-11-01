package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Merchant;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class AllMerchants {
    @SerializedName("merchant_list")
    @Expose
    private List<Merchant> merchantList = null;
    @SerializedName("all_suggestions")
    @Expose
    private List<String> allSuggestions = null;
    @SerializedName("version_side_menu_text")
    @Expose
    private String sideMenuText;
    @SerializedName("share_message")
    @Expose
    private String shareMessage;
    @SerializedName("coupon_message")
    @Expose
    private String couponMessage;

    public List<Merchant> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<Merchant> merchantList) {
        this.merchantList = merchantList;
    }

    public List<String> getAllSuggestions() {
        return allSuggestions;
    }

    public void setAllSuggestions(List<String> allSuggestions) {
        this.allSuggestions = allSuggestions;
    }


    public String getSideMenuText() {
        return sideMenuText;
    }

    public void setSideMenuText(String sideMenuText) {
        this.sideMenuText = sideMenuText;
    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }
}
