package com.slukovskyi.repositories;

import com.slukovskyi.models.Airline;
import com.slukovskyi.models.planes.Plane;

import java.util.List;

public interface PlaneRepository {
    List<Plane> getAll();

    Plane add(Plane plane);

    void delete(Plane plane);

    List<Plane> getAllByAirline(Airline airline);

    List<Plane> getAllWithoutAirline();

    List<Plane> getAllWithAirline();

    Plane getById(Long id);

    void update(Plane plane);
}
