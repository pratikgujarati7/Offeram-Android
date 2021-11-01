package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.transactionsModel;

import java.util.List;

public class TransactionResponse {
    @SerializedName("transactions")
    @Expose
    private List<transactionsModel> transactions = null;
    @SerializedName("offeram_coin_balance")
    @Expose
    private String offeram_coin_balance;

    public List<transactionsModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<transactionsModel> transactions) {
        this.transactions = transactions;
    }

    public String getOfferam_coin_balance() {
        return offeram_coin_balance;
    }

    public void setOfferam_coin_balance(String offeram_coin_balance) {
        this.offeram_coin_balance = offeram_coin_balance;
    }
}
