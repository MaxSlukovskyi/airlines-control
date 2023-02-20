package com.slukovskyi.repositories;

import com.slukovskyi.models.Airline;

import java.util.List;

public interface AirlineRepository {
    List<Airline> getAll();

    Airline getById(Long id);

    Airline add(Airline airline);

    void delete(Airline airline);

    void update(Airline airline);
}