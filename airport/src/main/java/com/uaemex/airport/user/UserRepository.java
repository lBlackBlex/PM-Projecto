package com.uaemex.airport.user;

import com.uaemex.airport.plane.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
    List<User> findAllByPlanesContains(Plane plane);

}
