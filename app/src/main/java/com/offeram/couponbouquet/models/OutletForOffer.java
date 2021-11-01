package com.offeram.couponbouquet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 10-Apr-18.
 */

public class OutletForOffer {
    @SerializedName("city_id")
    @Expose
    private Integer city_id;
    @SerializedName("city_name")
    @Expose
    private String city_name;
    @SerializedName("area_name")
    @Expose
    private String areaName;
    @SerializedName("distance")
    @Expose
    private Double distance;


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
