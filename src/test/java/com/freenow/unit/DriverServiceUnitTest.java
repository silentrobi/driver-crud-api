package com.freenow.unit;

import com.freenow.controller.util.QueryParams;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.dataaccessobject.specification.DriverSpecification;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import com.freenow.service.driver.DefaultDriverService;
import com.freenow.service.driver.DriverService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class DriverServiceUnitTest {

    @MockBean
    DriverRepository driverRepository;

    @TestConfiguration
    static class DefaultDriverServiceContextConfiguration {

        @Autowired
        DriverRepository driverRepository;

        @Autowired
        CarService carService;

        @Bean
        public DriverService driverService() {
            return new DefaultDriverService(driverRepository, carService);
        }
    }

    @Autowired
    DriverService driverService;

    @Before
    public void setup() {
        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        DriverDO driverDO2 = new DriverDO("driver02", "54321");
        DriverDO driverDO3 = new DriverDO("driver03", "12121");
        DriverDO driverDO4 = new DriverDO("driver04", "13579");

        List<DriverDO> allDrivers = Arrays.asList(driverDO1, driverDO2, driverDO3, driverDO4);

        Mockito.when(driverRepository.findAll(Specification.where(any()))).thenReturn(allDrivers);
    }

    @Test
    public void getDriverList() {
        // When
        List<DriverDO> found = driverService.find(new QueryParams());

        // Then
        Assert.assertEquals(4, found.size());
    }

    @Test
    public void GetDriverByDriverId() throws Exception {

        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        Mockito.when(driverRepository.findById(driverDO1.getId())).thenReturn(Optional.of(driverDO1));
        DriverDO found = driverService.find(driverDO1.getId());

        Assert.assertEquals(driverDO1.getId(), found.getId());
    }

    @Test
    public void GetExceptionByInvalidDriverId() throws Exception {
        Long driverId = 100L;

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.find(driverId);
        });
    }

    @Test
    public void createDriverWithSameUsername_ThrowException() throws Exception {
        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        Mockito.when(driverRepository.save(driverDO1)).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertThrows(ConstraintsViolationException.class, () -> {
            driverService.create(driverDO1);
        });
    }

    @Test
    public void createDriver() throws Exception {
        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        Mockito.when(driverRepository.save(driverDO1)).thenReturn(driverDO1);

        DriverDO newDriverDO = driverService.create(driverDO1);

        Assert.assertEquals(driverDO1.getUsername(), newDriverDO.getUsername());
    }

    @Test
    public void updateDriverLocationShouldThrowError() {
        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        Mockito.when(driverRepository.findById(driverDO1.getId())).thenReturn(Optional.of(driverDO1));

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.updateLocation(12L, 70.43, 44.55);
        });
    }

    @Test
    public void deleteDriverShouldThrowError() {
        DriverDO driverDO1 = new DriverDO("driver01", "12345");
        Mockito.when(driverRepository.findById(driverDO1.getId())).thenReturn(Optional.of(driverDO1));

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.delete(12L);
        });
    }

}
