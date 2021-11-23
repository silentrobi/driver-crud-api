package com.freenow.integration;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.datatransferobject.UpdateCarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import com.freenow.service.driver.DriverService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FreeNowServerApplicantTestApplication.class})
public class CarServiceIntegrationTest {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @AfterAll
    @Before
    public void resetDb() {
        carRepository.deleteAll();
    }

    private CarDO createTestCar(String licensePlate, Integer seatCount, Boolean convertible, String model, double rating, EngineType engineType, Manufacturer manufacturer) {
        CarDO carDO = new CarDO(licensePlate, seatCount, convertible, model, rating, engineType, manufacturer);
        return carRepository.save(carDO);
    }

    @Test
    public void whenCreateCar_withAlreadyExistLicensePlate_ThenThrowsConstraintsViolationException() {

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        CarDO targetCarDO = new CarDO(
                "12AXCD12",
                6,
                false,
                "2018",
                0.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );
        Assertions.assertThrows(ConstraintsViolationException.class, () -> {
            carService.create(targetCarDO);
        });
    }

    @Test
    public void whenValidInput_ThenCreateCar() throws Exception {

        CarDO carDO = new CarDO(
                "12AXCD13",
                6,
                false,
                "2018",
                0.0,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        CarDO newCarDto = carService.create(carDO);

        Assert.assertEquals(carDO.getLicensePlate(), newCarDto.getLicensePlate());
    }

    @Test
    public void whenValidInput_ThenUpdateCar() throws Exception {

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        UpdateCarDTO updateCarDTO = new UpdateCarDTO(
                "12AXCD12",
                8.0,
                EngineType.GAS);

        carService.update(carDO.getId(), updateCarDTO);

        CarDO found = carService.find(carDO.getId());

        Assert.assertEquals(updateCarDTO.getRating(), found.getRating());
        Assert.assertEquals(updateCarDTO.getEngineType(), found.getEngineType());
    }

    @Test
    public void whenAlreadyExistLicensePlate_ThenUpdateCar_throwConstraintsViolationException() throws Exception {

        CarDO carDO1 = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        CarDO targetDO = createTestCar(
                "12AXCD13",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        UpdateCarDTO updateCarDTO = new UpdateCarDTO(
                "12AXCD12",
                8.0,
                EngineType.GAS);

        Assertions.assertThrows(ConstraintsViolationException.class, () -> {
            carService.update(targetDO.getId(), updateCarDTO);
        });
    }

    @Test
    public void findCarByInvalidCarId_throwConstraintsViolationException() throws Exception {

        CarDO carDO1 = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carService.find(100L);
        });
    }

    @Test
    public void findCarByValidCarId() throws Exception {

        CarDO carDO = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        CarDO found = carService.find(carDO.getId());

        Assert.assertEquals(carDO.getId(), found.getId());
        Assert.assertEquals(carDO.getLicensePlate(), found.getLicensePlate());
    }

    @Test
    public void findAllCars() throws Exception {

        CarDO carDO1 = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        CarDO carDO2 = createTestCar(
                "12AXCD13",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.MERCEDEZ_BENCZ
        );
        CarDO carDO3 = createTestCar(
                "12AXCD14",
                5,
                false,
                "2016",
                0.0,
                EngineType.ELECTRIC,
                Manufacturer.BMW
        );

        List<CarDO> found = carService.find();

        Assert.assertEquals(3, found.size());
    }

    @Test
    public void deleteCarByInvalidCarId_throwConstraintsViolationException() {

        CarDO carDO1 = createTestCar(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carService.delete(100L);
        });
    }

    @Test
    public void deleteCarByValidCarId_throwConstraintsViolationException() throws Exception{

        CarDO carDO = createTestCar(
                "54AFCD12",
                5,
                false,
                "2011",
                0.0,
                EngineType.ELECTRIC,
                Manufacturer.MERCEDEZ_BENCZ
        );

        carService.delete(carDO.getId());


        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carService.find(carDO.getId());
        });
    }
}
