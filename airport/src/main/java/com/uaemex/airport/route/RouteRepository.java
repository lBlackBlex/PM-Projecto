package com.uaemex.airport.route;

import com.uaemex.airport.airline.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {
    Optional<Route> findRouteByPlane_Id(UUID planeId);
    List<Route> findAllByDateAndToAndFrom(Date date, String to, String from);
    List<Route> findAllByAirlinesContains(Airline airline);
    @Query("select r from Route r where r.from = ?2 and r.to = ?1 and r.date = ?3 and r.plane is not null")
    List<Route> findRoutesByDateAndToAndFrom(String to, String from, Date depart);
}
