package com.slukovskyi.services;

import com.slukovskyi.models.Airline;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.repositories.AirlineRepository;
import com.slukovskyi.repositories.impl.AirlineDbRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public final class AirlineService {

    private static AirlineService instance;

    private AirlineRepository airlineRepository;

    private AirlineService() {
        this.airlineRepository = AirlineDbRepositoryImpl.getInstance();
    }

    public static AirlineService getInstance() {
        if (instance == null) {
            instance = new AirlineService();
        }
        return instance;
    }

    public List<Airline> getAll() {
        List<Airline> airlines = airlineRepository.getAll();
        if (airlines.isEmpty()) {
            log.warn("IN getAll - there are no airlines");
        }
        log.info("IN getAll - {} airlines found", airlines.size());
        return airlines;
    }

    public Long getTotalSeatsNumberByAirline(Airline airline) {
        Long totalSeatsNumber = 0L;
        for (Plane plane : airline.getPlanes()) {
            totalSeatsNumber += plane.getSeatsNumber();
        }
        return totalSeatsNumber;
    }

    public Long getTotalCargoCapacityByAirline(Airline airline) {
        Long totalCargoCapacity = 0L;
        for (Plane plane : airline.getPlanes()) {
            totalCargoCapacity += plane.getCargoCapacity();
        }
        return totalCargoCapacity;
    }

    public Airline getById(Long id) {
        Airline airline = airlineRepository.getById(id);
        if (airline == null) {
            log.warn("There is no airline with id = {}", id);
            return null;
        }
        log.info("IN getById - airline with id = {} found", airline.getId());
        return airline;
    }

    public void add(Airline airline) {
        airlineRepository.add(airline);
        log.info("IN add - {} airline added", airline.getName());
    }

    public void update(Airline airline) {
        airlineRepository.update(airline);
        log.info("IN update - airline with id = {} was updated", airline.getId());
    }

    public void delete(Airline airline) {
        airlineRepository.delete(airline);
        log.info("IN delete - airline with id = {} was deleted", airline.getId());
    }

}
