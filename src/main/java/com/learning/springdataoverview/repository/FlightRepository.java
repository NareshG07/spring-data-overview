package com.learning.springdataoverview.repository;

import com.learning.springdataoverview.entity.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FlightRepository extends PagingAndSortingRepository<Flight,Long> {
}
