package com.slukovskyi.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.utils.annotations.Exclude;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Airline {
    private Long id;
    private String name;
    @Exclude
    private List<Plane> planes = new ArrayList<>();

    public Airline(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Airline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

