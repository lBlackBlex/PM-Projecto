package com.uaemex.airport.airline;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/airlines")
public class AirlineController {
    private final AirlineService airlineService;

    @GetMapping
    public List<Airline> getAirlines(){
        return airlineService.getAirlines();
    }

    @PostMapping
    public void registerNewAirline(@RequestBody Airline airline){
        airlineService.addNewAirline(airline);
    }

    @PutMapping(path = "{airlineId}")
    public void updateAirline(
            @PathVariable("airlineId") UUID airlineId,
            @RequestParam String name){
        airlineService.updateAirline(airlineId, name);
    }

    @DeleteMapping(path = "{airlineId}")
    public void deleteAirline(
            @PathVariable("airlineId") UUID airlineId){
        airlineService.deleteAirline(airlineId);
    }
}
