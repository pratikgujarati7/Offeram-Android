package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 03-Apr-18.
 */

public class Notification {
    @SerializedName("notification_id")
    @Expose
    private Integer notificationId;
    @SerializedName("notification_title")
    @Expose
    private String notificationTitle;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;
    @SerializedName("from_name")
    @Expose
    private String fromName;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
