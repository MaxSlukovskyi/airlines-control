package com.slukovskyi.models.planes;

import lombok.*;
import com.slukovskyi.models.Airline;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class Plane {
    private Long id;
    private String name;
    private Integer seatsNumber;
    private Integer cargoCapacity;
    private Integer maximumDistance;
    private Integer fuelConsumption;

    private Airline airline;

    public Plane(String name, Integer seatsNumber, Integer cargoCapacity,
                 Integer maximumDistance, Integer fuelConsumption) {
        this.name = name;
        this.seatsNumber = seatsNumber;
        this.cargoCapacity = cargoCapacity;
        this.maximumDistance = maximumDistance;
        this.fuelConsumption = fuelConsumption;
    }

}
