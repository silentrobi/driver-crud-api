package com.freenow.dataaccessobject.specification;

import com.freenow.controller.util.QueryParams;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DriverSpecification {

    public static Specification<DriverDO> findByQueryParams(QueryParams queryParams) {

        return ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (queryParams.getUsername() != null) {
                predicateList.add(criteriaBuilder.like(root.get("username"), "%" + queryParams.getUsername() + "%"));
            }
            if (queryParams.getOnlineStatus() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("onlineStatus"), queryParams.getOnlineStatus()));
            }

            final Path<CarDO> carPath = root.get("car");

            if (queryParams.getLicensePlate() != null) {
                predicateList.add(criteriaBuilder.like(carPath.<String>get("licensePlate"), "%" + queryParams.getLicensePlate() + "%"));
            }
            if (queryParams.getRating() >= 0.0) {
                predicateList.add(criteriaBuilder.equal(carPath.<String>get("rating"), queryParams.getRating()));
            }

            if (queryParams.getEngineType() != null) {
                predicateList.add(criteriaBuilder.equal(carPath.<String>get("engineType"), queryParams.getEngineType()));
            }
            if (queryParams.getManufacturer() != null) {
                predicateList.add(criteriaBuilder.equal(carPath.<String>get("manufacturer"), queryParams.getManufacturer()));
            }

            predicateList.add(criteriaBuilder.equal(root.get("deleted"), false));

            return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
        });
    }
}
