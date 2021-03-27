package com.uaemex.airport.route;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/routes")
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    public List<Route> getRoutes(){
        return routeService.getRoutes();
    }

    @PostMapping
    public void registerNewRoute(@RequestBody Route route){
        routeService.createNewRoute(route);
    }

    @PutMapping(path = "{routeId}")
    public void updateRoute(
            @PathVariable("routeId") UUID routeId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) Timestamp etd,
            @RequestParam(required = false) Timestamp eta,
            @RequestParam(required = false) Float cost){
        routeService.updateRoute(routeId, from, to, etd, eta, cost);
    }

    @DeleteMapping(path = "{routeId}")
    public void deleteRoute(@PathVariable("routeId") UUID routeId){
        routeService.deleteRoute(routeId);
    }
}
