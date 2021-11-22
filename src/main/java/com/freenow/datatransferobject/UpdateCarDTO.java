package com.freenow.datatransferobject;

import com.freenow.domainvalue.EngineType;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class UpdateCarDTO {

    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @DecimalMin(value = "0.0", message = "rating can not be lower than 0.0")
    @DecimalMax(value = "10.0", message = "rating can not be higher than 10.0")
    private Double rating;

    @NotNull(message = "Engine type can not be null!")
    private EngineType engineType;

    public UpdateCarDTO() {
    }

    public UpdateCarDTO(String licensePlate, Double rating, EngineType engineType) {
        this.licensePlate = licensePlate;
        this.rating = rating;
        this.engineType = engineType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }
}
