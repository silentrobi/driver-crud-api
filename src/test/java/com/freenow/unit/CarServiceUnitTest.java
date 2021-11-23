package com.freenow.unit;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.datatransferobject.UpdateCarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import com.freenow.service.car.DefaultCarService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Test
    public void GetCarByCarId() throws Exception {
        CarDO carDO = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.GAS,
                Manufacturer.FORD
        );
        Mockito.when(carRepository.findById(carDO.getId())).thenReturn(Optional.of(carDO));
        CarDO found = carService.find(carDO.getId());

        Assert.assertEquals(carDO.getLicensePlate(), found.getLicensePlate());
    }

    @Test
    public void GetExceptionByInvalidCarId() {
        Long carId = 100L;

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carService.find(carId);
        });
    }

    @Test
    public void createCarWithSameLicensePlate_ThrowException() {
        CarDO carDO = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.GAS,
                Manufacturer.FORD
        );
        Mockito.when(carRepository.save(carDO)).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertThrows(ConstraintsViolationException.class, () -> {
            carService.create(carDO);
        });
    }

    @Test
    public void createCar() throws Exception {
        CarDO carDO = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                0.0,
                EngineType.GAS,
                Manufacturer.FORD
        );
        Mockito.when(carRepository.save(carDO)).thenReturn(carDO);

        CarDO newCarDO = carService.create(carDO);

        Assert.assertEquals(carDO.getLicensePlate(), newCarDO.getLicensePlate());
    }

    @Test
    public void updateCar_shouldThrowExceptionForSameLicensePlate() {
        CarDO carDO = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                7.9,
                EngineType.DIESEL,
                Manufacturer.FORD
        );

        Mockito.when(carRepository.findById(carDO.getId())).thenReturn(Optional.of(carDO));
        Mockito.when(carRepository.save(carDO)).thenThrow(DataIntegrityViolationException.class);
        ;

        UpdateCarDTO updateCarDTO = new UpdateCarDTO();
        updateCarDTO.setEngineType(EngineType.DIESEL);
        updateCarDTO.setLicensePlate("12AXCD12");
        updateCarDTO.setRating(7.9);

        Assertions.assertThrows(ConstraintsViolationException.class, () -> {
            carService.update(carDO.getId(), updateCarDTO);
        });
    }

    @Test
    public void updateCar_shouldThrowExceptionForInvalidCarId() {

        UpdateCarDTO updateCarDTO = new UpdateCarDTO();
        updateCarDTO.setEngineType(EngineType.DIESEL);
        updateCarDTO.setLicensePlate("12AXCD12");
        updateCarDTO.setRating(7.9);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carService.update(100L, updateCarDTO);
        });
    }

    @Test
    public void deleteCarShouldThrowError() {
        CarDO carDO = new CarDO(
                "12AXCD12",
                5,
                false,
                "2016",
                7.9,
                EngineType.DIESEL,
                Manufacturer.FORD
        );
        Mockito.when(carRepository.findById(carDO.getId())).thenReturn(Optional.of(carDO));

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carService.delete(12L);
        });
    }
}
