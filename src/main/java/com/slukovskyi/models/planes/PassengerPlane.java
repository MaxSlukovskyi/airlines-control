package com.slukovskyi.models.planes;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PassengerPlane extends Plane {
    private Integer businessClassSeatsNumber;
    private Integer firstClassSeatsNumber;
    private Integer economyClassSeatsNumber;

    public PassengerPlane(String name, Integer seatsNumber, Integer cargoCapacity, Integer maximumDistance,
                          Integer fuelConsumption, Integer businessClassSeatsNumber, Integer firstClassSeatsNumber,
                          Integer economyClassSeatsNumber) {
        super(name, seatsNumber, cargoCapacity, maximumDistance, fuelConsumption);
        this.businessClassSeatsNumber = businessClassSeatsNumber;
        this.firstClassSeatsNumber = firstClassSeatsNumber;
        this.economyClassSeatsNumber = economyClassSeatsNumber;
    }

    @Override
    public String toString() {
        return "PassengerPlane{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", seatsNumber=" + this.getSeatsNumber() +
                ", cargoCapacity=" + this.getCargoCapacity() +
                ", maximumDistance=" + this.getMaximumDistance() +
                ", fuelConsumption=" + this.getFuelConsumption() +
                ", airline=" + this.getAirline() +
                ", businessClassSeatsNumber=" + this.getBusinessClassSeatsNumber() +
                ", firstClassSeatsNumber=" + this.getFirstClassSeatsNumber() +
                ", economyClassSeatsNumber=" + this.getEconomyClassSeatsNumber() +
                '}';
    }
}