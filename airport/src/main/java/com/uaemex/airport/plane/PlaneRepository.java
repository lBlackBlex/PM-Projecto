package com.uaemex.airport.plane;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlaneRepository extends JpaRepository<Plane, UUID> {
    Optional<Plane> findPlaneByModel(String model);
}
