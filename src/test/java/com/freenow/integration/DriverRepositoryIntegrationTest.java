package com.freenow.integration;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.exception.EntityNotFoundException;
import org.junit.After;
import org.junit.Test;
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

    @After
    private void cleanDB(){
        entityManager.createQuery("delete from DriverDO").executeUpdate();
    }

    @Test
    public void whenFindById_thenThrowsEntityNotFoundException() {

        cleanDB();

        // given
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

        cleanDB();

        // given
        DriverDO driver = new DriverDO(
                "driver01",
                "12345"
        );

        entityManager.persist(driver);
        entityManager.flush();

        // when
        DriverDO found = driverRepository.findById(driver.getId()).orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driver.getId()));;

        // then
        assertEquals(driver.getUsername(), found.getUsername());
    }

    @Test
    public void whenSave_thenReturnNewDriver() {

        cleanDB();

        // given
        DriverDO driver = new DriverDO(
                "driver01",
                "12345"
        );

        // when
        DriverDO driverDO = driverRepository.save(driver);

        // then
        assertEquals(driver.getUsername(), driverDO.getUsername());
        assertEquals(driver.getPassword(), driverDO.getPassword());
    }

    @Test
    public void whenSaveWithSameUsername_thenThrowsDataIntegrityViolationException() {

        cleanDB();

        DriverDO driver = new DriverDO(
                "driver01",
                "12345"
        );

        entityManager.persist(driver);
        entityManager.flush();


        //given
        DriverDO driverWithSameUsername = new DriverDO(
                "driver01",
                "12121"
        );

        //when and then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            driverRepository.save(driverWithSameUsername);
        });
    }
}
