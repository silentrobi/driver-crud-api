package com.freenow.unit;

import com.freenow.controller.util.QueryParams;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.service.car.CarService;
import com.freenow.service.car.DefaultCarService;
import com.freenow.service.driver.DefaultDriverService;
import com.freenow.service.driver.DriverService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class CarServiceUnitTest {

    @MockBean
    CarRepository carRepository;

    @TestConfiguration
    static class DefaultCarServiceContextConfiguration {

        @Autowired
        CarRepository carRepository;

        @Bean
        public CarService carService() {
            return new DefaultCarService(carRepository);
        }
    }

    @Autowired
    CarService carService;

    @Test
    public void getCarList() {
        // Given
        CarDO carDO1 = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.GAS,
                Manufacturer.FORD
        );
        CarDO carDO2 = new CarDO(
                "12AXCD13",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        Mockito.when(carRepository.findAll()).thenReturn(Arrays.asList(carDO1, carDO2));

        // When
        List<CarDO> found = carService.find();

        // Then
        Assert.assertEquals(2, found.size());
    }
}
