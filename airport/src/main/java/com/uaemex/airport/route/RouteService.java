package com.uaemex.airport.route;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@AllArgsConstructor
@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public Route getRoute(UUID routeId){
        Optional<Route> routeOptional = routeRepository.findById(routeId);
        if (routeOptional.isEmpty())
            throw new IllegalStateException("Route with id " + routeId + "does not exist");
        return routeOptional.get();
    }

    public List<Route> getRoutes(){
        return routeRepository.findAll();
    }
    public void createNewRoute(Route route){
        //TODO que no se repita el avion en tiempos de ruta
        routeRepository.save(route);
    }

    //TODO modificar FK (tickets, boardin_room, plane)
    @Transactional
    public void updateRoute(UUID routeId, String from, String to, Timestamp etd, Timestamp eta, Float cost){
        //TODO modificar FK's (airline, terminal, plane)
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + " does not exist"));

        if (from != null && from.length() > 0 && !Objects.equals(route.getFrom(), from)) route.setFrom(from);
        if (to != null && to.length() > 0 && !Objects.equals(route.getTo(), to)) route.setTo(to);

        Date now = new Date(Timestamp.from(Instant.now()).getTime());
        Date date_etd = new Date(etd.getTime());
        Date date_eta = new Date(eta.getTime());

        //TODO que la fecha no sea antes de el dia que se crea
        if (now.before(date_etd) && !route.getEtd().equals(etd)) route.setEtd(etd);
        if (now.before(date_eta) && !route.getEta().equals(eta)) route.setEta(eta);

        if(cost > 0 && !Objects.equals(route.getCost(), cost)) route.setCost(cost);
    }

    public void deleteRoute(UUID routeId){
        boolean exists = routeRepository.existsById(routeId);
        if (!exists) throw new IllegalStateException("Route with id " + routeId + " does not exist");
        routeRepository.deleteById(routeId);

    }
}
