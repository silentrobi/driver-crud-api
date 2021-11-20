package com.freenow.service.car;

import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;

import java.util.List;

public interface CarService {

    CarDO find(Long carId) throws EntityNotFoundException;

    List<CarDO> find();

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    CarDO update(CarDO carDO) throws ConstraintsViolationException;

    void delete(Long carId) throws EntityNotFoundException;
}
