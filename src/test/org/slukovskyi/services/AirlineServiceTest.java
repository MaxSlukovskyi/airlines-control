package org.slukovskyi.services;

import com.slukovskyi.repositories.impl.AirlineDbRepositoryImpl;
import com.slukovskyi.services.AirlineService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private AirlineDbRepositoryImpl airlineRepository;

    @InjectMocks
    private AirlineService airlineService;

    @Test
    void getAll() {
        List<Airline> expectedAirlines = this.getTestAirlines();
        when(airlineRepository.getAll()).thenReturn(expectedAirlines);
        List<Airline> actualAirline = airlineService.getAll();
        assertEquals(expectedAirlines, actualAirline);
        verify(airlineRepository, times(1)).getAll();
    }

    @Test
    void getById() {
        Airline expectedAirline = this.getAirlineWithPlanes();
        when(airlineRepository.getById(expectedAirline.getId())).thenReturn(expectedAirline);
        Airline actualAirline = airlineService.getById(expectedAirline.getId());
        assertEquals(expectedAirline, actualAirline);
        verify(airlineRepository, times(1)).getById(expectedAirline.getId());
    }

    @Test
    void getTotalSeatsNumberByAirline() {
        Airline airline = this.getAirlineWithPlanes();
        Long expectedResult = 135L;
        Long actualResult = airlineService.getTotalSeatsNumberByAirline(airline);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getTotalCargoCapacityByAirline() {
        Airline airline = this.getAirlineWithPlanes();
        Long expectedResult = 25000L;
        Long actualResult = airlineService.getTotalCargoCapacityByAirline(airline);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void add() {
        Airline airline = this.getAirlineWithPlanes();
        airlineService.add(airline);
        verify(airlineRepository, times(1)).add(airline);
    }

    @Test
    void update() {
        Airline airline = this.getAirlineWithPlanes();
        airlineService.update(airline);
        verify(airlineRepository, times(1)).update(airline);
    }

    @Test
    void delete() {
        Airline airline = this.getAirlineWithPlanes();
        airlineService.delete(airline);
        verify(airlineRepository, times(1)).delete(airline);
    }

    private List<Airline> getTestAirlines() {
        List<Airline> airlines = new ArrayList<>();

        Airline firstAirline = new Airline("IUA");
        firstAirline.setId(1L);
        Airline secondAirline = new Airline("WizzAir");
        secondAirline.setId(2L);
        Airline thirdAirline = new Airline("TestAirline");
        thirdAirline.setId(3L);
        Airline fourthAirline = new Airline("TestAirline2");
        fourthAirline.setId(4L);

        airlines.add(firstAirline);
        airlines.add(secondAirline);
        airlines.add(thirdAirline);
        airlines.add(fourthAirline);

        return airlines;
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
