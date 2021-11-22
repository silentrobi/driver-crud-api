package com.freenow.integration;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.controller.util.QueryParams;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.DriverOfflineException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DriverService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FreeNowServerApplicantTestApplication.class})
public class DriverServiceIntegrationTest {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarRepository carRepository;

    @AfterAll
    @Before
    public void resetDb() {
        driverRepository.deleteAll();
        carRepository.deleteAll();
    }

    private CarDO createTestCar(String licensePlate, Integer seatCount, Boolean convertible, String model, double rating, EngineType engineType, Manufacturer manufacturer) {
        CarDO carDO = new CarDO(licensePlate, seatCount, convertible, model, rating, engineType, manufacturer);
        return carRepository.save(carDO);
    }

    private DriverDO createTestDriver(String username, String password) {
        DriverDO driverDO = new DriverDO(username, password);
        return driverRepository.save(driverDO);
    }

    @Test
    public void whenAlreadyExistUsernae_ThenThrowsConstraintsViolationException() {

        DriverDO driverDO = createTestDriver("driver01", "12345");

        DriverDO targetDriverDO = new DriverDO("driver01", "41321");
        Assertions.assertThrows(ConstraintsViolationException.class, () -> {
            driverService.create(targetDriverDO);
        });
    }

    @Test
    public void whenValidInput_ThenThrowsConstraintsViolationException() throws Exception {

        DriverDO targetDriverDO = new DriverDO("driver01", "41321");

        DriverDO driverDO = driverService.create(targetDriverDO);

        Assert.assertEquals(targetDriverDO.getUsername(), driverDO.getUsername());
    }

    @Test
    public void whenValidInput_UpdateLocation() throws Exception {

        DriverDO driverDO = createTestDriver("driver01", "12345");

        driverService.updateLocation(driverDO.getId(), 78.09, 40.43);

        DriverDO found = driverService.find(driverDO.getId());

        Assert.assertEquals(40.43, found.getCoordinate().getLatitude(), 0);
        Assert.assertEquals(78.09, found.getCoordinate().getLongitude(), 0);
    }

    @Test
    public void selectCar_whenInvalidDriver_ThrowEntityNotFoundException() throws Exception {

        DriverDO driverDO = createTestDriver("driver01", "12345");

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.selectCar(100L, carDO.getId());
        });
    }

    @Test
    public void selectCar_whenOfflineDriver_ThrowDriverOfflineException() throws Exception {

        DriverDO driverDO = createTestDriver("driver01", "12345");

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        Assertions.assertThrows(DriverOfflineException.class, () -> {
            driverService.selectCar(driverDO.getId(), carDO.getId());
        });
    }

    @Test
    public void selectCar_whenInvalidCarId_ThrowEntityNotFoundException() throws Exception {

        DriverDO driverDO = createTestDriver("driver01", "12345");

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        Assertions.assertThrows(DriverOfflineException.class, () -> {
            driverService.selectCar(driverDO.getId(), 100L);
        });
    }

    @Test
    public void selectCar_whenAlreadyInUse_ThrowCarAlreadyInUseException() throws Exception {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "12345");
        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        driverDO1.setCar(carDO);
        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);
        driverDO2.setOnlineStatus(OnlineStatus.ONLINE);

        driverRepository.saveAll(Arrays.asList(driverDO1, driverDO2));

        Assertions.assertThrows(CarAlreadyInUseException.class, () -> {
            driverService.selectCar(driverDO2.getId(), carDO.getId());
        });
    }

    @Test
    public void selectCar_whenValidOnlineDriver_ThrowCarAlreadyInUseException() throws Exception {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        driverRepository.save(driverDO1);

        driverService.selectCar(driverDO1.getId(), carDO.getId());

        DriverDO targetDriverDO = driverService.find(driverDO1.getId());

        Assert.assertEquals(carDO.getId(), targetDriverDO.getCar().getId());
        Assert.assertEquals(EngineType.DIESEL, targetDriverDO.getCar().getEngineType());
        Assert.assertEquals(Manufacturer.FORD, targetDriverDO.getCar().getManufacturer());
    }

    @Test
    public void selectCar_whenValidOnlineDriver() throws Exception {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        driverRepository.save(driverDO1);

        driverService.selectCar(driverDO1.getId(), carDO.getId());

        DriverDO targetDriverDO = driverService.find(driverDO1.getId());

        Assert.assertEquals(carDO.getId(), targetDriverDO.getCar().getId());
        Assert.assertEquals(EngineType.DIESEL, targetDriverDO.getCar().getEngineType());
        Assert.assertEquals(Manufacturer.FORD, targetDriverDO.getCar().getManufacturer());
    }

    @Test
    public void deselectCar_whenValidOnlineDriver() throws Exception {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        driverRepository.save(driverDO1);

        driverService.deselectCar(driverDO1.getId());

        DriverDO targetDriverDO = driverService.find(driverDO1.getId());

        Assert.assertEquals(null, targetDriverDO.getCar());
    }

    @Test
    public void deselectCar_whenInvalidOnlineDriver_throwEntityNotFoundException() throws Exception {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.deselectCar(100L);
        });
    }

    @Test
    public void findDriverByInvalidId_throwEntityNotFoundException() {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.find(100L);
        });
    }


    @Test
    public void findDriverByValidId_returnDriver() throws Exception {

        DriverDO driverDO1 = createTestDriver("driver01", "12345");

        DriverDO found = driverService.find(driverDO1.getId());

        Assert.assertEquals(driverDO1.getId(), found.getId());
    }

    @Test
    public void findAllDrivers() throws Exception {

        QueryParams queryParams = new QueryParams();

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "12345");
        DriverDO driverDO3 = createTestDriver("driver03", "12345");

        List<DriverDO> found = driverService.find(queryParams);

        Assert.assertEquals(3, found.size());
    }

    @Test
    public void findAllDrivers_ByOfflineStatus() throws Exception {

        QueryParams queryParams = new QueryParams();
        queryParams.setOnlineStatus(OnlineStatus.OFFLINE);

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "12345");
        DriverDO driverDO3 = createTestDriver("driver03", "12345");
        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        driverRepository.saveAll(Arrays.asList(driverDO1));

        List<DriverDO> found = driverService.find(queryParams);

        Assert.assertEquals(2, found.size());
    }

    @Test
    public void findAllDrivers_ByManufacturer() throws Exception {

        QueryParams queryParams = new QueryParams();
        queryParams.setManufacturer(Manufacturer.AUDI);

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "12345");
        DriverDO driverDO3 = createTestDriver("driver03", "12345");
        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        CarDO carDO1 = createTestCar(
                "12AXCD13",
                5,
                false,
                "2017",
                7.0,
                EngineType.GAS,
                Manufacturer.AUDI
        );

        CarDO carDO2 = createTestCar(
                "12AXCD14",
                5,
                false,
                "2016",
                5.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        driverDO1.setCar(carDO1);
        driverDO2.setCar(carDO2);

        driverRepository.saveAll(Arrays.asList(driverDO1, driverDO2));

        List<DriverDO> found = driverService.find(queryParams);

        Assert.assertEquals(2, found.size());
    }

    @Test
    public void findAllDrivers_ByManufacturerAndEngineType() throws Exception {

        QueryParams queryParams = new QueryParams();
        queryParams.setManufacturer(Manufacturer.AUDI);
        queryParams.setEngineType(EngineType.DIESEL);

        DriverDO driverDO1 = createTestDriver("driver01", "12345");
        DriverDO driverDO2 = createTestDriver("driver02", "12345");
        DriverDO driverDO3 = createTestDriver("driver03", "12345");
        driverDO1.setOnlineStatus(OnlineStatus.ONLINE);

        CarDO carDO1 = createTestCar(
                "12AXCD13",
                5,
                false,
                "2017",
                7.0,
                EngineType.GAS,
                Manufacturer.AUDI
        );

        CarDO carDO2 = createTestCar(
                "12AXCD14",
                5,
                false,
                "2016",
                5.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        driverDO1.setCar(carDO1);
        driverDO2.setCar(carDO2);

        driverRepository.saveAll(Arrays.asList(driverDO1, driverDO2));

        List<DriverDO> found = driverService.find(queryParams);

        Assert.assertEquals(1, found.size());
    }

    @Test
    public void deleteByInvalidDriverId_throwsEntityNotFoundException() {
        DriverDO driverDO1 = createTestDriver("driver01", "12345");

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverService.delete(100L);
        });
    }

    @Test
    public void deleteByValidDriverId() throws Exception{
        DriverDO driverDO1 = createTestDriver("driver01", "12345");

        driverService.delete(driverDO1.getId());

        DriverDO found = driverService.find(driverDO1.getId());  //

        Assertions.assertEquals(null, found);
    }

}
