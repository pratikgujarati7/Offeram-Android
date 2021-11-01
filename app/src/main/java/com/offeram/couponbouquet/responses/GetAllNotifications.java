package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Notification;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class GetAllNotifications {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("notify_unread_list")
    @Expose
    private List<Notification> notifyUnreadList = null;
    @SerializedName("notify_read_list")
    @Expose
    private List<Notification> notifyReadList = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<Notification> getNotifyUnreadList() {
        return notifyUnreadList;
    }

    public void setNotifyUnreadList(List<Notification> notifyUnreadList) {
        this.notifyUnreadList = notifyUnreadList;
    }

    public List<Notification> getNotifyReadList() {
        return notifyReadList;
    }

    public void setNotifyReadList(List<Notification> notifyReadList) {
        this.notifyReadList = notifyReadList;
    }
}
