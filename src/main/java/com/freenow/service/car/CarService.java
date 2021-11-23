package com.freenow.service.car;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.datatransferobject.UpdateCarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface CarService {

    CarDO find(Long carId) throws EntityNotFoundException;

    List<CarDO> find();

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    void update(Long carId, UpdateCarDTO updateCarDTO) throws ConstraintsViolationException, EntityNotFoundException;

    void delete(Long carId) throws EntityNotFoundException, ConstraintsViolationException;
}
