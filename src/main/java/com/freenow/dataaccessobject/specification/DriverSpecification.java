package com.freenow.dataaccessobject.specification;

import com.freenow.controller.util.QueryParams;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DriverSpecification {

    public static Specification<DriverDO> findByQueryParams(QueryParams queryParams){

        return ((root, criteriaQuery, criteriaBuilder) ->{
            List<Predicate> predicateList = new ArrayList<>();
            if(queryParams.getUsername() != null ){
                predicateList.add( criteriaBuilder.like(root.get("username"), queryParams.getUsername()));
            }
            if(queryParams.getOnlineStatus() != null){
                predicateList.add(criteriaBuilder.equal(root.get("onlineStatus"), queryParams.getOnlineStatus()));
            }

            final Path<CarDO> brandPath = root.get("car");

            if(queryParams.getLicensePlate() != null){
                predicateList.add(criteriaBuilder.like(brandPath.<String>get("licensePlate"), queryParams.getLicensePlate()));
            }
            if(queryParams.getRating() != 0.0){
                predicateList.add(criteriaBuilder.equal(brandPath.<String>get("rating"), queryParams.getRating()));
            }
            if(queryParams.getManufacturer() != null){
                predicateList.add(criteriaBuilder.equal(brandPath.<String>get("manufacturer"), queryParams.getManufacturer()));
            }

            return criteriaBuilder.and(predicateList.toArray(Predicate[]::new));
        });
    }
}
