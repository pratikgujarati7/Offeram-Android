package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 07-Sep-17.
 */

public class GenerateChecksum {
    @SerializedName("CHECKSUMHASH")
    @Expose
    private String checksumHash;
    @SerializedName("ORDER_ID")
    @Expose
    private String orderId;
    @SerializedName("payt_STATUS")
    @Expose
    private String paytmStatus;


    public String getChecksumHash() {
        return checksumHash;
    }

    public void setChecksumHash(String checksumHash) {
        this.checksumHash = checksumHash;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaytmStatus() {
        return paytmStatus;
    }

    public void setPaytmStatus(String paytmStatus) {
        this.paytmStatus = paytmStatus;
    }
}
