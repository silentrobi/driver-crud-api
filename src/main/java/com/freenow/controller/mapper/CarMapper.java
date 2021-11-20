package com.freenow.controller.mapper;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {
    public static CarDO makeCarDO(CarDTO carDTO) {
        return new CarDO(
                carDTO.getLicensePlate(),
                carDTO.getSeatCount(),
                carDTO.getConvertible(),
                carDTO.getModel(),
                carDTO.getRating(),
                carDTO.getEngineType(),
                carDTO.getManufacturer()
        );
    }

    public static CarDTO makeCarDTO(CarDO carDO) {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
                .setId(carDO.getId())
                .setLicensePlate(carDO.getLicensePlate())
                .setConvertible(carDO.getConvertible())
                .setEngineType(carDO.getEngineType())
                .setModel(carDO.getModel())
                .setRating(carDO.getRating())
                .setSeatCount(carDO.getSeatCount())
                .setManufacturer(carDO.getManufacturer());

        return carDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeDriverDTOList(Collection<CarDO> cars) {
        return cars.stream()
                .map(CarMapper::makeCarDTO)
                .collect(Collectors.toList());
    }
}
