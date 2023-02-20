package com.slukovskyi.models.planes;

import lombok.*;
import com.slukovskyi.models.enums.JetType;

@Getter
@Setter
@NoArgsConstructor
public class JetPlane extends Plane {

    private JetType jetType;

    public JetPlane(String name, Integer seatsNumber, Integer cargoCapacity,
                    Integer maximumDistance, Integer fuelConsumption, JetType jetType) {
        super(name, seatsNumber, cargoCapacity, maximumDistance, fuelConsumption);
        this.jetType = jetType;
    }

    @Override
    public String toString() {
        return "JetPlane{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", seatsNumber=" + this.getSeatsNumber() +
                ", cargoCapacity=" + this.getCargoCapacity() +
                ", maximumDistance=" + this.getMaximumDistance() +
                ", fuelConsumption=" + this.getFuelConsumption() +
                ", airline=" + this.getAirline() +
                ", jetType=" + this.getJetType() +
                '}';
    }
}