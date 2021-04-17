package com.uaemex.airport.airline;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class AirlineService {
    private final AirlineRepository airlineRepository;
    private final RouteRepository routeRepository;

    public Airline getAirline(UUID airlineId) {
        return airlineRepository.findById(airlineId)
                .orElseThrow(()-> new IllegalStateException("Airline with id " + airlineId + " does not exist"));
    }

    public List<Airline> getAirlines() {
        return airlineRepository.findAll();
    }

    public void addNewAirline(Airline airline) {
        Optional<Airline> airlineOptional = airlineRepository.findAirlineByName(airline.getName());

        if (airlineOptional.isPresent()) throw new IllegalStateException("Name already in use");
        airlineRepository.save(airline);
    }

    @Transactional
    public void updateAirline(UUID airlineId, String name) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalStateException("Airline with id " + airlineId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(airline.getName(), name)) airline.setName(name);
    }


    public void deleteAirline(UUID airlineId) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalStateException("Airline with id " + airlineId + " does not exist"));

        List<Route> routes = routeRepository.findAllByAirlinesContains(airline);
        routes.forEach(route -> route.getAirlines().remove(airline));
        airlineRepository.deleteById(airlineId);
    }
}
