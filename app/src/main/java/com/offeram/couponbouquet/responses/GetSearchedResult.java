package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.Area;
import com.offeram.couponbouquet.models.SearchItem;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class GetSearchedResult {
    @SerializedName("coupons_list")
    @Expose
    private List<SearchItem> couponsList = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<SearchItem> getSearchList() {
        return couponsList;
    }

    public void setSearchList(List<SearchItem> couponsList) {
        this.couponsList = couponsList;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
