package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO {

    private Long id;

    private String licensePlate;

    private Integer seatCount;

    private Boolean convertible;

    private String model;

    private Double rating;

    private EngineType engineType;

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

        @JsonIgnore
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

        public CarDTO.CarDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public CarDTO createCarDTO() {
            return new CarDTO(id, licensePlate, seatCount, convertible, model, rating, engineType, manufacturer);
        }
    }
}
