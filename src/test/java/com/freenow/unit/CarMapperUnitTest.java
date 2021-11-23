package com.freenow.unit;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CarMapperUnitTest {

    @Test
    public void test_makeCarDO() {
        CarDTO carDTO = CarDTO.newBuilder()
                .setLicensePlate("12ZX234")
                .setEngineType(EngineType.DIESEL)
                .setRating(6.7)
                .setModel("2010")
                .setManufacturer(Manufacturer.FORD)
                .createCarDTO();

        CarDO carDO = CarMapper.makeCarDO(carDTO);

        Assert.assertEquals(carDTO.getLicensePlate(), carDO.getLicensePlate());
        Assert.assertEquals(carDTO.getManufacturer(), carDO.getManufacturer());
    }

    @Test
    public void test_makeCarDTO() {
        CarDO carDO = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        CarDTO carDTO = CarMapper.makeCarDTO(carDO);

        Assert.assertEquals(carDO.getLicensePlate(), carDTO.getLicensePlate());
        Assert.assertEquals(carDO.getManufacturer(), carDTO.getManufacturer());
    }

    @Test
    public void test_makeCarDTOList() {
        CarDO carDO1 = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        CarDO carDO2 = new CarDO(
                "12AXCD13",
                5,
                false,
                "2016",
                0.0,
                EngineType.ELECTRIC,
                Manufacturer.FORD
        );

        List<CarDTO> found = CarMapper.makeCarDTOList(Arrays.asList(carDO1, carDO2));

        Assert.assertEquals(2, found.size());
    }
}
