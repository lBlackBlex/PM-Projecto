package com.uaemex.airport.plane;

import com.uaemex.airport.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaneRepository extends JpaRepository<Plane, UUID> {
    Optional<Plane> findPlaneByModel(String model);
}
