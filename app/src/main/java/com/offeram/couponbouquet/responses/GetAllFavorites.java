package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.Area;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.SearchItem;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class GetAllFavorites {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("starred_list")
    @Expose
    private List<SearchItem> starredList = null;
    @SerializedName("pinged_list")
    @Expose
    private List<Merchant> pingedList = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<SearchItem> getStarredList() {
        return starredList;
    }

    public void setStarredList(List<SearchItem> starredList) {
        this.starredList = starredList;
    }

    public List<Merchant> getPingedList() {
        return pingedList;
    }

    public void setPingedList(List<Merchant> pingedList) {
        this.pingedList = pingedList;
    }
}
