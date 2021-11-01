package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Area;
import com.offeram.couponbouquet.models.Category;
import com.offeram.couponbouquet.models.City;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.RedeemersModel;

import java.util.List;

/**
 * Created by admin on 07-Sep-17.
 */

public class Common {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("is_update")
    @Expose
    private Integer isUpdate;
    @SerializedName("count_for_notification")
    @Expose
    private Integer countForNotification;
    @SerializedName("all_suggestions")
    @Expose
    private List<String> allSuggestions = null;
    @SerializedName("all_areas")
    @Expose
    private List<Area> allAreas = null;
    @SerializedName("all_city")
    @Expose
    private List<City> allCity = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("purchase_price")
    @Expose
    private String purchasePrice;
    // Below are the params for getProfileDetails
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;  // For profile picture
    @SerializedName("user_contact")
    @Expose
    private String userContact;
    @SerializedName("valid_date")
    @Expose
    private String validDate;

    @SerializedName("userofferamcoinbalance")
    @Expose
    private String userofferamcoinbalance;

    @SerializedName("referal_url")
    @Expose
    private String referal_url;

    @SerializedName("referal_code")
    @Expose
    private String referal_code;


    @SerializedName("top_ten_redeemers_list")
    @Expose
    private List<RedeemersModel> toptenredeemerslist = null;

    @SerializedName("is_tambola_active")
    @Expose
    private String is_tambola_active;
    @SerializedName("is_ipl_active")
    @Expose
    private String is_ipl_active;

    @SerializedName("is_tambola_register")
    @Expose
    private String is_tambola_register;

    @SerializedName("ticket_url")
    @Expose
    private String ticket_url;

    @SerializedName("tambola_title")
    @Expose
    private String tambola_title;

    @SerializedName("tambola_desc")
    @Expose
    private String tambola_desc;

    @SerializedName("tambola_image_url")
    @Expose
    private String tambola_image_url;

    @SerializedName("tambola_detail_url")
    @Expose
    private String tambola_detail_url;

    public String getIs_ipl_active() {
        return is_ipl_active;
    }

    public void setIs_ipl_active(String is_ipl_active) {
        this.is_ipl_active = is_ipl_active;
    }

    public String getReferAndEarnText() {
        return ReferAndEarnText;
    }

    public void setReferAndEarnText(String referAndEarnText) {
        ReferAndEarnText = referAndEarnText;
    }

    public String getOfferamCoinsLabel1Text() {
        return OfferamCoinsLabel1Text;
    }

    public void setOfferamCoinsLabel1Text(String offeramCoinsLabel1Text) {
        OfferamCoinsLabel1Text = offeramCoinsLabel1Text;
    }

    public String getOfferamCoinsLabel2Text() {
        return OfferamCoinsLabel2Text;
    }

    public void setOfferamCoinsLabel2Text(String offeramCoinsLabel2Text) {
        OfferamCoinsLabel2Text = offeramCoinsLabel2Text;
    }

    public String getOfferamCoinsLabel3Text() {
        return OfferamCoinsLabel3Text;
    }

    public void setOfferamCoinsLabel3Text(String offeramCoinsLabel3Text) {
        OfferamCoinsLabel3Text = offeramCoinsLabel3Text;
    }

    public String getOfferamCoinsLabel4Text() {
        return OfferamCoinsLabel4Text;
    }

    public void setOfferamCoinsLabel4Text(String offeramCoinsLabel4Text) {
        OfferamCoinsLabel4Text = offeramCoinsLabel4Text;
    }

    @SerializedName("ReferAndEarnText")
    @Expose
    private String ReferAndEarnText;

    @SerializedName("OfferamCoinsLabel1Text")
    @Expose
    private String OfferamCoinsLabel1Text;

    @SerializedName("OfferamCoinsLabel2Text")
    @Expose
    private String OfferamCoinsLabel2Text;

    @SerializedName("OfferamCoinsLabel3Text")
    @Expose
    private String OfferamCoinsLabel3Text;

    @SerializedName("OfferamCoinsLabel4Text")
    @Expose
    private String OfferamCoinsLabel4Text;



    public String getIs_tambola_active() {
        return is_tambola_active;
    }

    public void setIs_tambola_active(String is_tambola_active) {
        this.is_tambola_active = is_tambola_active;
    }

    public String getIs_tambola_register() {
        return is_tambola_register;
    }

    public void setIs_tambola_register(String is_tambola_register) {
        this.is_tambola_register = is_tambola_register;
    }

    public String getTicket_url() {
        return ticket_url;
    }

    public void setTicket_url(String ticket_url) {
        this.ticket_url = ticket_url;
    }

    public String getTambola_title() {
        return tambola_title;
    }

    public void setTambola_title(String tambola_title) {
        this.tambola_title = tambola_title;
    }

    public String getTambola_desc() {
        return tambola_desc;
    }

    public void setTambola_desc(String tambola_desc) {
        this.tambola_desc = tambola_desc;
    }

    public String getTambola_image_url() {
        return tambola_image_url;
    }

    public void setTambola_image_url(String tambola_image_url) {
        this.tambola_image_url = tambola_image_url;
    }

    public String getTambola_detail_url() {
        return tambola_detail_url;
    }

    public void setTambola_detail_url(String tambola_detail_url) {
        this.tambola_detail_url = tambola_detail_url;
    }

    public String getUserofferamcoinbalance() {
        return userofferamcoinbalance;
    }

    public void setUserofferamcoinbalance(String userofferamcoinbalance) {
        this.userofferamcoinbalance = userofferamcoinbalance;
    }

    public String getReferal_url() {
        return referal_url;
    }

    public void setReferal_url(String referal_url) {
        this.referal_url = referal_url;
    }

    public String getReferal_code() {
        return referal_code;
    }

    public void setReferal_code(String referal_code) {
        this.referal_code = referal_code;
    }

    public List<RedeemersModel> getToptenredeemerslist() {
        return toptenredeemerslist;
    }

    public void setToptenredeemerslist(List<RedeemersModel> toptenredeemerslist) {
        this.toptenredeemerslist = toptenredeemerslist;
    }

    public List<City> getAllCity() {
        return allCity;
    }

    public void setAllCity(List<City> allCity) {
        this.allCity = allCity;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<String> getAllSuggestions() {
        return allSuggestions;
    }

    public void setAllSuggestions(List<String> allSuggestions) {
        this.allSuggestions = allSuggestions;
    }

    public List<Area> getAllAreas() {
        return allAreas;
    }

    public void setAllAreas(List<Area> allAreas) {
        this.allAreas = allAreas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Integer getCountForNotification() {
        return countForNotification;
    }

    public void setCountForNotification(Integer countForNotification) {
        this.countForNotification = countForNotification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
