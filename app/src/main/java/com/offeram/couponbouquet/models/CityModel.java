package com.offeram.couponbouquet.models;

public class CityModel {

    int id;
    String strCityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }

    public void setAll(String title) {
        setStrCityName(title);
    }
}
