package com.freenow.integration;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainvalue.EngineType;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.exception.EntityNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;


    @Test
    public void whenFindById_thenThrowsEntityNotFoundException() throws Exception {
        // given
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
        // given
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

        // when
        CarDO found = carRepository.findById(car.getId()).orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + car.getId()));;

        // then
        assertEquals(car.getLicensePlate(), found.getLicensePlate());
    }
}
