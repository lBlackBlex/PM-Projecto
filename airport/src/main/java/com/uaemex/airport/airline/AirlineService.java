package com.uaemex.airport.airline;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AirlineService {
    private final AirlineRepository airlineRepository;

    public Airline getAirline(UUID airlineId) {
        Optional<Airline> airlineOptional = airlineRepository.findById(airlineId);
        if (airlineOptional.isEmpty())
            throw new IllegalStateException("Airline with id " + airlineId + "does not exist");
        return airlineOptional.get();
    }

    public List<Airline> getAirlines() {
        return airlineRepository.findAll();
    }

    public void addNewAirline(Airline airline) {
        Optional<Airline> airlineOptional = airlineRepository.findAirlineByName(airline.getName());

        if (airlineOptional.isPresent()) throw new IllegalStateException("Name already in use");
        airlineRepository.save(airline);
    }

    //TODO modificar FK ruta
    @Transactional
    public void updateAirline(UUID airlineId, String name) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalStateException("Airline with id " + airlineId + " does not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(airline.getName(), name)) airline.setName(name);
    }


    public void deleteAirline(UUID airlineId) {
        boolean exist = airlineRepository.existsById(airlineId);
        if (!exist) throw new IllegalStateException("User with id " + airlineId + " does not exist");
        airlineRepository.deleteById(airlineId);
    }
}
