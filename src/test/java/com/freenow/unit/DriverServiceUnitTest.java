package com.freenow.unit;

import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.service.car.CarService;
import com.freenow.service.driver.DefaultDriverService;
import com.freenow.service.driver.DriverService;
import org.junit.Before;
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
public class DriverServiceUnitTest {

    @MockBean
    DriverRepository driverRepository;

    @MockBean
    CarService carService;

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

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

        CarDO carDO3 = new CarDO(
                "12AXCD14",
                5,
                false,
                "2016",
                0.0,
                EngineType.ELECTRIC,
                Manufacturer.BMW
        );

        CarDO carDO4 = new CarDO(
                "12AXCD15",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        driverDO1.setCar(carDO1);
        driverDO2.setCar(carDO2);
        driverDO3.setCar(carDO3);
        driverDO4.setCar(carDO4);

        List<DriverDO> allDrivers = Arrays.asList(driverDO1, driverDO2, driverDO3, driverDO4);

        Mockito.when(driverRepository.findAll()).thenReturn(allDrivers);
    }

}
