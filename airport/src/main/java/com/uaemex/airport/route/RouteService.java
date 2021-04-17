package com.uaemex.airport.route;

import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.airline.AirlineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final AirlineRepository airlineRepository;

    public Route getRoute(UUID routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(()-> new IllegalStateException("Route with id " + routeId + " does not exist"));
    }

    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }

    public List<Route> getRouteByDate(String date, String to, String from) {
        return routeRepository.findAllByDateAndToAndFrom(java.sql.Date.valueOf(date), to, from);
    }

    public List<Route> getAvailableRoutes(String to, String from, String depart) {
        return routeRepository.findRoutesByDateAndToAndFrom(to, from, java.sql.Date.valueOf(depart));
    }

    public void createNewRoute(Route route) {
        routeRepository.save(route);
    }

    //TODO que no se repita el avion en tiempos de ruta
    @Transactional
    public void updateRoute(UUID routeId, java.sql.Date date, String from, String to, Timestamp etd, Timestamp eta, Float cost) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + " does not exist"));

        if (from != null && from.length() > 0 && !Objects.equals(route.getFrom(), from)) route.setFrom(from);
        if (to != null && to.length() > 0 && !Objects.equals(route.getTo(), to)) route.setTo(to);

        //TODO que la fecha no sea antes de el dia que se crea
        Date now = new Date(Timestamp.from(Instant.now()).getTime());
        if (date != null){
            if (now.before(date) && !route.getDate().equals(date)) route.setDate(date);
        }
        if (etd != null) {
            Date date_etd = new Date(etd.getTime());
            if (now.before(date_etd) && !route.getEtd().equals(etd)) route.setEtd(etd);
        }
        if (eta != null) {
            Date date_eta = new Date(eta.getTime());
            if (now.before(date_eta) && !route.getEta().equals(eta)) route.setEta(eta);
        }

        if (cost != null && cost > 0 && !Objects.equals(route.getCost(), cost)) route.setCost(cost);
    }

    @Transactional
    public void addRouteAirline(UUID routeId, UUID airlineId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + " does not exist"));
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalStateException("Airline with id " + airlineId + " does not exist"));

        route.getAirlines().add(airline);
        airline.getRoutes().add(route);
    }

    @Transactional
    public void removeRouteAirline(UUID routeId, UUID airlineId){
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + " does not exist"));
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new IllegalStateException("Airline with id " + airlineId + "does not exist"));

        route.getAirlines().remove(airline);
    }

    public void deleteRoute(UUID routeId) {
        boolean exists = routeRepository.existsById(routeId);
        if (!exists) throw new IllegalStateException("Route with id " + routeId + " does not exist");
        routeRepository.deleteById(routeId);

    }
}
