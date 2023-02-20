package com.slukovskyi.services;

import lombok.extern.slf4j.Slf4j;
import com.slukovskyi.models.Airline;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.repositories.PlaneRepository;
import com.slukovskyi.repositories.impl.PlaneDbRepositoryImpl;

import java.util.Comparator;
import java.util.List;

@Slf4j
public final class PlaneService {

    private static PlaneService instance;
    private PlaneRepository planeRepository;

    private PlaneService() {
        this.planeRepository = PlaneDbRepositoryImpl.getInstance();
    }

    public static PlaneService getInstance() {
        if (instance == null) {
            instance = new PlaneService();
        }
        return instance;
    }

    public List<Plane> getAll() {
        List<Plane> planes = planeRepository.getAll();
        if (planes.isEmpty()) {
            log.warn("IN getAll - there are no planes");
        }
        log.info("IN getAll - {} planes found", planes.size());
        return planes;
    }

    public Plane getById(Long id) {
        Plane plane = planeRepository.getById(id);
        if (plane == null) {
            log.warn("There is no plane with id = {}", id);
            return null;
        }
        log.info("IN getById - plane with id = {} found", plane.getId());
        return plane;
    }

    public void add(Plane plane) {
        planeRepository.add(plane);
        log.info("IN add - {} plane added", plane.getName());
    }

    public List<Plane> sortAirlinePlanesByMaximumDistance(Airline airline) {
        return airline.getPlanes().stream().sorted(Comparator.comparing(Plane::getMaximumDistance)).toList();
    }

    public List<Plane> getAirlinePlanesByFuelConsumption(Integer minimumConsumption, Integer maximumConsumption,
                                                         Airline airline) {
        List<Plane> filteredPlanes = airline.getPlanes().stream().filter(plane -> plane.getAirline().getId().equals(airline.getId()))
                .filter(plane -> plane.getFuelConsumption() >= minimumConsumption
                        && plane.getFuelConsumption() <= maximumConsumption).toList();
        log.info("IN getAirlinePlanesByFuelConsumption - {} planes found", filteredPlanes.size());
        return filteredPlanes;
    }

    public List<Plane> getPlanesWithoutAirline() {
        List<Plane> planes = planeRepository.getAllWithoutAirline();
        if (planes.isEmpty()) {
            log.warn("IN getPlanesWithAirline - there are no planes without airlines");
        }
        log.info("IN getPlanesWithoutAirline - {} planes found", planes.size());
        return planes;
    }

    public List<Plane> getPlanesWithAirline() {
        List<Plane> planes = planeRepository.getAllWithAirline();
        if (planes.isEmpty()) {
            log.warn("IN getPlanesWithAirline - there are no planes with airlines");
        }
        log.info("IN getPlanesWithAirline - {} planes found", planes.size());
        return planes;
    }

    public void update(Plane plane) {
        planeRepository.update(plane);
        log.info("IN update - plane with id = {} was updated", plane.getId());
    }

    public void delete(Plane plane) {
        planeRepository.delete(plane);
        log.info("IN delete - plane with id = {} was deleted", plane.getId());
    }

    public void addPlaneToAirline(Plane plane, Airline airline) {
        plane.setAirline(airline);
        this.update(plane);
        log.info("IN addPlaneToAirline - plane with id = {} was added to airline with id = {}",
                plane.getId(), airline.getId());
    }

    public void deletePlaneFromAirline(Plane plane) {
        plane.setAirline(null);
        this.update(plane);
        log.info("IN addPlaneToAirline - plane with id = {} was deleted from airline", plane.getId());
    }

}

