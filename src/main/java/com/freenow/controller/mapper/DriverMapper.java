package com.freenow.controller.mapper;

import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DriverMapper {

    public static DriverDO makeDriverDO(DriverDTO driverDTO) {
        return new DriverDO(driverDTO.getUsername(), driverDTO.getPassword());
    }

    public static DriverDTO makeDriverDTO(DriverDO driverDO) {
        DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
                .setId(driverDO.getId())
                .setPassword(driverDO.getPassword())
                .setUsername(driverDO.getUsername());

        final GeoCoordinate coordinate = driverDO.getCoordinate();
        final CarDO car = driverDO.getCar();

        if (coordinate != null) {
            driverDTOBuilder.setCoordinate(coordinate);
        }
        if (car != null) {
            driverDTOBuilder.setCar(CarMapper.makeCarDTO(car));
        }

        return driverDTOBuilder.createDriverDTO();
    }

    public static List<DriverDTO> makeDriverDTOList(Collection<DriverDO> drivers) {
        return drivers.stream()
                .map(DriverMapper::makeDriverDTO)
                .collect(Collectors.toList());
    }
}
