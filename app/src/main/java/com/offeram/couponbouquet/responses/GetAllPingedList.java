package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Category;
import com.offeram.couponbouquet.models.Merchant;

import java.util.List;

public class GetAllPingedList {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pinged_list")
    @Expose
    private List<Merchant> pinged_list = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Merchant> getPinged_list() {
        return pinged_list;
    }

    public void setPinged_list(List<Merchant> pinged_list) {
        this.pinged_list = pinged_list;
    }
}
