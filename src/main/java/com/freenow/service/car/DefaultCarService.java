package com.freenow.service.car;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.datatransferobject.UpdateCarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultCarService implements CarService{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;

    public DefaultCarService(final CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }

    /**
     * Selects a car by id.
     *
     * @param carId
     * @return found car
     * @throws EntityNotFoundException if no car with the given id was found.
     */
    @Override
    public CarDO find(Long carId) throws EntityNotFoundException {
        return findCarChecked(carId);
    }

    /**
     * Selects all cars.
     *
     * @return found car list
     */
    @Override
    public List<CarDO> find() {
        return Lists.newArrayList(carRepository.findAll().iterator());
    }

    /**
     * Creates a new car.
     *
     * @param carDO
     * @return
     * @throws ConstraintsViolationException if a car already exists with the given licensePlate, ... .
     */
    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException {
        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }

    @Override
    @Transactional
    public void update(Long carId, UpdateCarDTO updateCarDTO) throws ConstraintsViolationException, EntityNotFoundException {
        CarDO carDO = findCarChecked(carId);

        carDO.setLicensePlate(updateCarDTO.getLicensePlate());
        carDO.setEngineType(updateCarDTO.getEngineType());
        carDO.setRating(updateCarDTO.getRating());
    }

    /**
     * Deletes an existing driver by id.
     *
     * @param carId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException {
        CarDO driverDO = findCarChecked(carId);
        driverDO.setDeleted(true);
    }

    private CarDO findCarChecked(Long carId) throws EntityNotFoundException
    {
        return carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + carId));
    }
}
