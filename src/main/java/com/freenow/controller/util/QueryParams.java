package com.freenow.controller.util;

import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.OnlineStatus;

public class QueryParams {

    private String username;
    private OnlineStatus onlineStatus;
    private String licensePlate;
    private double rating;
    private EngineType engineType;
    private Manufacturer manufacturer;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "QueryParams{" +
                "username='" + username + '\'' +
                ", onlineStatus=" + onlineStatus +
                ", licensePlate='" + licensePlate + '\'' +
                ", rating=" + rating +
                ", engineType=" + engineType +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
