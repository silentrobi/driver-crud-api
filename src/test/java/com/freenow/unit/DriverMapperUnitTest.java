package com.freenow.unit;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.controller.mapper.DriverMapper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DriverMapperUnitTest {

    @Test
    public void test_makeDriverDO(){
        DriverDTO driverDTO = DriverDTO.newBuilder()
                .setUsername("driver01")
                .setPassword("12345")
                .createDriverDTO();

        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);

        Assert.assertEquals(driverDTO.getUsername(), driverDO.getUsername());
        Assert.assertEquals(driverDTO.getPassword(), driverDO.getPassword());
    }

    @Test
    public void test_makeDriverDTOList() {
        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        DriverDO driverDO2 = new DriverDO("driver02", "54321");


        List<DriverDTO> found = DriverMapper.makeDriverDTOList(Arrays.asList(driverDO1,driverDO2));

        Assert.assertEquals(2, found.size());
    }

    @Test
    public void test_makeCarDTO(){
        DriverDO driverDO = new DriverDO("driver01", "12345");

        DriverDTO driverDTO = DriverMapper.makeDriverDTO(driverDO);

        Assert.assertEquals(driverDO.getUsername(), driverDTO.getUsername());
        Assert.assertEquals(driverDO.getPassword(), driverDTO.getPassword());
    }
}
