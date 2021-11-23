package com.freenow.integration;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.exception.EntityNotFoundException;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Before
    @AfterAll
    public void cleanDB() {
        entityManager.createQuery("delete from CarDO").executeUpdate();
    }

    @Test
    public void whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        CarDO car = new CarDO(
                "AZ0123",
                5,
                false,
                "2015",
                null,
                EngineType.DIESEL,
                Manufacturer.BMW
        );

        entityManager.persist(car);
        entityManager.flush();

        Long id = 12L;
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            carRepository.findById(12L).orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + id));
        });
    }

    @Test
    public void whenFindById_thenReturnCar() throws Exception {
        // Given
        CarDO car = new CarDO(
                "AZ0123",
                5,
                false,
                "2015",
                null,
                EngineType.DIESEL,
                Manufacturer.BMW
        );

        entityManager.persist(car);
        entityManager.flush();

        // When
        CarDO found = carRepository.findById(car.getId()).orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + car.getId()));
        ;

        // Then
        assertEquals(car.getLicensePlate(), found.getLicensePlate());
    }

    @Test
    public void whenSave_thenReturnNewCar() {
        // Given
        CarDO car = new CarDO(
                "AZ0123",
                5,
                false,
                "2015",
                null,
                EngineType.DIESEL,
                Manufacturer.BMW
        );

        // When
        CarDO carDO = carRepository.save(car);

        // Then
        assertEquals(car.getLicensePlate(), carDO.getLicensePlate());
        assertEquals(car.getEngineType(), carDO.getEngineType());
        assertEquals(car.getConvertible(), carDO.getConvertible());
        assertEquals(car.getManufacturer(), carDO.getManufacturer());
    }

    @Test
    public void whenSaveWithSameLicensePlate_thenThrowsDataIntegrityViolationException() {
        // Given
        CarDO car = new CarDO(
                "AZ0123",
                5,
                false,
                "2015",
                null,
                EngineType.DIESEL,
                Manufacturer.BMW
        );

        entityManager.persist(car);
        entityManager.flush();

        CarDO carWithSameLicensePlate = new CarDO(
                "AZ0123", //same license plate
                5,
                false,
                "2018",
                null,
                EngineType.DIESEL,
                Manufacturer.AUDI
        );

        // When and Then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            carRepository.save(carWithSameLicensePlate);
        });
    }

    @Test
    public void whenFind_thenReturnAllCars() {
        // Given
        CarDO car1 = new CarDO(
                "AZ0123",
                5,
                false,
                "2015",
                null,
                EngineType.DIESEL,
                Manufacturer.BMW
        );

        CarDO car2 = new CarDO(
                "AZ0124",
                5,
                false,
                "2015",
                null,
                EngineType.ELECTRIC,
                Manufacturer.BMW
        );

        entityManager.persist(car1);
        entityManager.flush();

        entityManager.persist(car2);
        entityManager.flush();

        // When
        List<CarDO> found = Lists.newArrayList(carRepository.findAll().iterator());

        // Then
        assertEquals(2, found.size());
        assertEquals(car1.getLicensePlate(), found.get(0).getLicensePlate());
        assertEquals(car1.getEngineType(), found.get(0).getEngineType());
        assertEquals(car2.getLicensePlate(), found.get(1).getLicensePlate());
        assertEquals(car2.getEngineType(), found.get(1).getEngineType());
    }
}
