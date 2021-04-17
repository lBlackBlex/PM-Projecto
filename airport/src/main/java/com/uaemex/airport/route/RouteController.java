package com.uaemex.airport.route;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/routes")
public class RouteController {
    private final RouteService routeService;

    @GetMapping(path = "/{routeId}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Route getRoute(@PathVariable("routeId") UUID routeId){
        return routeService.getRoute(routeId);
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<Route> getRoutes(){
        return routeService.getRoutes();
    }

    @GetMapping()
    public List<Route> getRouteByDate(
            @RequestParam() String date,
            @RequestParam() String to,
            @RequestParam() String from){
        return routeService.getRouteByDate(date, to, from);
    }

    @GetMapping("/available")
    public List<Route> getAvailableRoutes(
            @RequestParam() String to,
            @RequestParam() String from,
            @RequestParam() String depart){
        return routeService.getAvailableRoutes(to, from, depart);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void registerNewRoute(@RequestBody Route route){
        routeService.createNewRoute(route);
    }

    @PostMapping(path = "{routeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void addRouteAirline(
            @PathVariable("routeId") UUID routeId,
            @RequestParam UUID airlineId){
        routeService.addRouteAirline(routeId, airlineId);
    }

    @PutMapping(path = "{routeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void updateRoute(
            @PathVariable("routeId") UUID routeId,
            @RequestParam(required = false) Date date,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) Timestamp etd,
            @RequestParam(required = false) Timestamp eta,
            @RequestParam(required = false) Float cost){
        routeService.updateRoute(routeId, date, from, to, etd, eta, cost);
    }

    @DeleteMapping(path = "{routeId}/{airlineId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void removeRouteAirline(
            @PathVariable("routeId") UUID routeId,
            @PathVariable("airlineId") UUID airlineId){
        routeService.removeRouteAirline(routeId, airlineId);
    }

    @DeleteMapping(path = "{routeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteRoute(@PathVariable("routeId") UUID routeId){
        routeService.deleteRoute(routeId);
    }
}
