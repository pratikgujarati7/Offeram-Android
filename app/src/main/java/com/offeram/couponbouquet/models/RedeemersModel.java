package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemersModel {
    @SerializedName("redeemer_id")
    @Expose
    private Integer redeemer_id;
    @SerializedName("redeemer_profile_image_url")
    @Expose
    private String redeemer_profile_image_url;
    @SerializedName("redeemer_name")
    @Expose
    private String redeemer_name;
    @SerializedName("redeemed_amount")
    @Expose
    private String redeemed_amount;

    public Integer getRedeemer_id() {
        return redeemer_id;
    }

    public void setRedeemer_id(Integer redeemer_id) {
        this.redeemer_id = redeemer_id;
    }

    public String getRedeemer_profile_image_url() {
        return redeemer_profile_image_url;
    }

    public void setRedeemer_profile_image_url(String redeemer_profile_image_url) {
        this.redeemer_profile_image_url = redeemer_profile_image_url;
    }

    public String getRedeemer_name() {
        return redeemer_name;
    }

    public void setRedeemer_name(String redeemer_name) {
        this.redeemer_name = redeemer_name;
    }

    public String getRedeemed_amount() {
        return redeemed_amount;
    }

    public void setRedeemed_amount(String redeemed_amount) {
        this.redeemed_amount = redeemed_amount;
    }
}
