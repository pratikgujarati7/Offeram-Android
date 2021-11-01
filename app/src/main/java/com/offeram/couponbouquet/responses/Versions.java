package com.offeram.couponbouquet.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.offeram.couponbouquet.models.AllVersion;
import com.offeram.couponbouquet.models.PurchasedVersion;

import java.util.List;

/**
 * Created by admin on 14-Sep-17.
 */

public class Versions {
    @SerializedName("user_purchased_versions")
    @Expose
    private List<PurchasedVersion> purchasedVersions = null;
    @SerializedName("all_versions")
    @Expose
    private List<AllVersion> allVersions = null;

    public List<PurchasedVersion> getPurchasedVersions() {
        return purchasedVersions;
    }

    public void setPurchasedVersions(List<PurchasedVersion> userPurchasedVersions) {
        this.purchasedVersions = userPurchasedVersions;
    }

    public List<AllVersion> getAllVersions() {
        return allVersions;
    }

    public void setAllVersions(List<AllVersion> allVersions) {
        this.allVersions = allVersions;
    }

}
