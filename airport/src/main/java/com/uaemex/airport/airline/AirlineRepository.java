package com.uaemex.airport.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {
    Optional<Airline> findAirlineByName(String name);
}
