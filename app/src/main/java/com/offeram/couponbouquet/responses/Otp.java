package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 07-Sep-17.
 */

public class Otp {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("version_id")
    @Expose
    private String versionId;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("version_name")
    @Expose
    private String versionName;
    @SerializedName("is_fresh_user")
    @Expose
    private String is_fresh_user;

    public String getIs_fresh_user() {
        return is_fresh_user;
    }

    public void setIs_fresh_user(String is_fresh_user) {
        this.is_fresh_user = is_fresh_user;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
