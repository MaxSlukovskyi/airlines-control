package org.slukovskyi.services;

import com.slukovskyi.services.PlaneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.slukovskyi.models.Airline;
import com.slukovskyi.models.enums.JetType;
import com.slukovskyi.models.planes.JetPlane;
import com.slukovskyi.models.planes.PassengerPlane;
import com.slukovskyi.models.planes.Plane;
import com.slukovskyi.repositories.PlaneRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PlaneServiceTest {

    @Mock
    private PlaneRepository planeRepository;

    @InjectMocks
    private PlaneService planeService;

    @Test
    void getAll() {
        List<Plane> expectedPlanes = this.getTestPlanes();
        when(planeRepository.getAll()).thenReturn(expectedPlanes);
        List<Plane> actualPlanes = planeService.getAll();
        assertEquals(expectedPlanes, actualPlanes);
        verify(planeRepository, times(1)).getAll();
    }

    @Test
    void getById() {
        PassengerPlane expectedPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        expectedPlane.setId(1L);

        when(planeRepository.getById(1L)).thenReturn(expectedPlane);

        Plane actualPlane = planeService.getById(1L);

        assertEquals(expectedPlane, actualPlane);
        verify(planeRepository, times(1)).getById(1L);
    }

    @Test
    void getByIdWhenPlaneNotFound() {
        when(planeRepository.getById(1L)).thenReturn(null);

        Plane actualPlane = planeService.getById(1L);

        assertNull(actualPlane);
        verify(planeRepository, times(1)).getById(1L);
    }

    @Test
    void add() {
        PassengerPlane expectedPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        expectedPlane.setId(1L);

        planeService.add(expectedPlane);
        verify(planeRepository, times(1)).add(expectedPlane);
    }

    @Test
    void update() {
        PassengerPlane expectedPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        expectedPlane.setId(1L);

        planeService.update(expectedPlane);
        verify(planeRepository, times(1)).update(expectedPlane);
    }

    @Test
    void delete() {
        PassengerPlane expectedPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        expectedPlane.setId(1L);

        planeService.delete(expectedPlane);
        verify(planeRepository, times(1)).delete(expectedPlane);
    }

    @Test
    void getPlanesWithoutAirline() {
        List<Plane> expectedPlanes = this.getTestPlanes();
        when(planeRepository.getAllWithoutAirline()).thenReturn(expectedPlanes);

        List<Plane> actualPlanes = planeService.getPlanesWithoutAirline();
        assertEquals(expectedPlanes, actualPlanes);
        verify(planeRepository, times(1)).getAllWithoutAirline();
    }

    @Test
    void getPlanesWithAirline() {
        List<Plane> expectedPlanes = this.getTestPlanes();
        when(planeRepository.getAllWithAirline()).thenReturn(expectedPlanes);

        List<Plane> actualPlanes = planeService.getPlanesWithAirline();
        assertEquals(expectedPlanes, actualPlanes);
        verify(planeRepository, times(1)).getAllWithAirline();
    }

    @Test
    void addPlaneToAirline() {
        PassengerPlane plane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        plane.setId(1L);

        Airline airline = new Airline("WizzAir");
        airline.setId(1L);

        PassengerPlane expectedPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        expectedPlane.setId(1L);
        expectedPlane.setAirline(airline);

        planeService.addPlaneToAirline(plane, airline);
        verify(planeRepository, times(1)).update(expectedPlane);
    }

    @Test
    void deletePlaneFromAirline() {
        PassengerPlane plane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        plane.setId(1L);

        Airline airline = new Airline("WizzAir");
        airline.setId(1L);

        plane.setAirline(airline);

        PassengerPlane expectedPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        expectedPlane.setId(1L);
        expectedPlane.setAirline(null);

        planeService.deletePlaneFromAirline(plane);
        verify(planeRepository, times(1)).update(expectedPlane);
    }

    @Test
    void getAirlinePlanesByFuelConsumption() {
        Airline airline = this.getAirlineWithPlanes();
        List<Plane> expectedPlanes = new ArrayList<>();
        expectedPlanes.add(airline.getPlanes().get(0));
        List<Plane> actualPlanes = planeService.getAirlinePlanesByFuelConsumption(500, 700, airline);
        assertEquals(expectedPlanes, actualPlanes);
    }

    @Test
    void sortAirlinePlanesByMaximumDistance() {
        Airline airline = this.getAirlineWithPlanes();
        List<Plane> expectedPlanes = new ArrayList<>();
        expectedPlanes.add(airline.getPlanes().get(0));
        expectedPlanes.add(airline.getPlanes().get(1));
        List<Plane> actualPlanes = planeService.sortAirlinePlanesByMaximumDistance(airline);
        assertEquals(expectedPlanes, actualPlanes);
    }

    private List<Plane> getTestPlanes() {
        List<Plane> testPlanes = new ArrayList<>();
        PassengerPlane passengerPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        JetPlane jetPlane = new JetPlane("TestJet", 15, 5000, 4000, 300, JetType.MID_SIZE);
        testPlanes.add(passengerPlane);
        testPlanes.add(jetPlane);
        return testPlanes;
    }

    private Airline getAirlineWithPlanes() {
        Airline airline = new Airline("WizzAir");
        airline.setId(1L);

        List<Plane> testPlanes = new ArrayList<>();
        PassengerPlane passengerPlane = new PassengerPlane("Boeing-777", 120, 20000,
                3000, 600, 20, 40, 60);
        JetPlane jetPlane = new JetPlane("TestJet", 15, 5000, 4000, 300, JetType.MID_SIZE);
        passengerPlane.setAirline(airline);
        jetPlane.setAirline(airline);
        testPlanes.add(passengerPlane);
        testPlanes.add(jetPlane);

        airline.setPlanes(testPlanes);

        return airline;
    }

}
