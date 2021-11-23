package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO {

    private Long id;

    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @NotNull(message = "Seat count can not be null!")
    @Min(value = 1)
    private Integer seatCount;

    private Boolean convertible;

    @NotNull(message = "Model can not be null!")
    private String model;

    @DecimalMin(value = "0.0", message = "rating can not be lower than 0.0")

    @DecimalMax(value = "10.0", message = "rating can not be higher than 10.0")
    private Double rating;

    @NotNull(message = "Engine type can not be null!")
    private EngineType engineType;

    @NotNull(message = "Manufacturer can not be null!")
    private Manufacturer manufacturer;

    private CarDTO() {
    }

    private CarDTO(
            Long id,
            String licensePlate,
            Integer seatCount,
            Boolean convertible,
            String model,
            Double rating,
            EngineType engineType,
            Manufacturer manufacturer) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.model = model;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }

    public static CarDTO.CarDTOBuilder newBuilder() {
        return new CarDTO.CarDTOBuilder();
    }

    public Long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public Boolean getConvertible() {
        return convertible;
    }

    public String getModel() {
        return model;
    }

    public Double getRating() {
        return rating;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public static class CarDTOBuilder {

        private Long id;

        private String licensePlate;

        private Integer seatCount;

        private Boolean convertible;

        private String model;

        private Double rating;

        private EngineType engineType;

        private Manufacturer manufacturer;

        public Long getId() {
            return id;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public CarDTOBuilder setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
            return this;
        }

        public Integer getSeatCount() {
            return seatCount;
        }

        public CarDTOBuilder setSeatCount(Integer seatCount) {
            this.seatCount = seatCount;
            return this;
        }

        public Boolean getConvertible() {
            return convertible;
        }

        public CarDTOBuilder setConvertible(Boolean convertible) {
            this.convertible = convertible;
            return this;
        }

        public String getModel() {
            return model;
        }

        public CarDTOBuilder setModel(String model) {
            this.model = model;
            return this;
        }

        public Double getRating() {
            return rating;
        }

        public CarDTOBuilder setRating(Double rating) {
            this.rating = rating;
            return this;
        }

        public EngineType getEngineType() {
            return engineType;
        }

        public CarDTOBuilder setEngineType(EngineType engineType) {
            this.engineType = engineType;
            return this;
        }

        public Manufacturer getManufacturer() {
            return manufacturer;
        }

        public CarDTOBuilder setManufacturer(Manufacturer manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public CarDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public CarDTO createCarDTO() {
            return new CarDTO(id, licensePlate, seatCount, convertible, model, rating, engineType, manufacturer);
        }
    }
}
