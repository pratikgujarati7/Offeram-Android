package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class transactionsModel {
    @SerializedName("transaction_id")
    @Expose
    private String transaction_id;
    @SerializedName("transaction_reason")
    @Expose
    private String transaction_reason;
    @SerializedName("transaction_type")
    @Expose
    private String transaction_type;
    @SerializedName("transaction_amount")
    @Expose
    private String transaction_amount;
    @SerializedName("transaction_date_time")
    @Expose
    private String transaction_date_time;

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_reason() {
        return transaction_reason;
    }

    public void setTransaction_reason(String transaction_reason) {
        this.transaction_reason = transaction_reason;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransaction_date_time() {
        return transaction_date_time;
    }

    public void setTransaction_date_time(String transaction_date_time) {
        this.transaction_date_time = transaction_date_time;
    }
}
