package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.Area;
import com.offeram.couponbouquet.models.Merchant;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class GetAllOffers {
    @SerializedName("merchant_list")
    @Expose
    private List<AllOffer> AllOffer = null;
    @SerializedName("all_suggestions")
    @Expose
    private List<String> allSuggestions = null;
    @SerializedName("is_update")
    @Expose
    private Integer isUpdate;
    @SerializedName("version_side_menu_text")
    @Expose
    private String versionSideMenuText;
    @SerializedName("all_areas")
    @Expose
    private List<Area> allAreas = null;
    @SerializedName("share_message")
    @Expose
    private String shareMessage;
    @SerializedName("coupon_message")
    @Expose
    private String couponMessage;

    public List<AllOffer> getAllOffer() {
        return AllOffer;
    }

    public void setAllOffer(List<AllOffer> AllOffer) {
        this.AllOffer = AllOffer;
    }

    public List<String> getAllSuggestions() {
        return allSuggestions;
    }

    public void setAllSuggestions(List<String> allSuggestions) {
        this.allSuggestions = allSuggestions;
    }

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getVersionSideMenuText() {
        return versionSideMenuText;
    }

    public void setVersionSideMenuText(String versionSideMenuText) {
        this.versionSideMenuText = versionSideMenuText;
    }

    public List<Area> getAllAreas() {
        return allAreas;
    }

    public void setAllAreas(List<Area> allAreas) {
        this.allAreas = allAreas;
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
