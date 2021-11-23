package com.freenow.integration;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.exception.EntityNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DriverRepositoryIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DriverRepository driverRepository;

    @Before
    @AfterAll
    public void cleanDB(){
        entityManager.createQuery("delete from DriverDO").executeUpdate();
    }

    @Test
    public void whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        DriverDO driver = new DriverDO("driver01", "12345");

        entityManager.persist(driver);
        entityManager.flush();

        Long id = 12L;
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            driverRepository.findById(12L).orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + id));
        });
    }

    @Test
    public void whenFindById_thenReturnDriver() throws Exception {
        // Given
        DriverDO driver = new DriverDO(
                "driver01",
                "12345"
        );

        entityManager.persist(driver);
        entityManager.flush();

        // When
        DriverDO found = driverRepository.findById(driver.getId()).orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driver.getId()));;

        // Then
        assertEquals(driver.getUsername(), found.getUsername());
    }

    @Test
    public void whenSave_thenReturnNewDriver() {
        // Given
        DriverDO driver = new DriverDO(
                "driver01",
                "12345");

        // When
        DriverDO driverDO = driverRepository.save(driver);

        // Then
        assertEquals(driver.getUsername(), driverDO.getUsername());
        assertEquals(driver.getPassword(), driverDO.getPassword());
    }

    @Test
    public void whenSaveWithSameUsername_thenThrowsDataIntegrityViolationException() {
        // Given
        DriverDO driver = new DriverDO(
                "driver01",
                "12345"
        );

        entityManager.persist(driver);
        entityManager.flush();

        DriverDO driverWithSameUsername = new DriverDO(
                "driver01",
                "12121"
        );

        // When and Then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            driverRepository.save(driverWithSameUsername);
        });
    }
}
