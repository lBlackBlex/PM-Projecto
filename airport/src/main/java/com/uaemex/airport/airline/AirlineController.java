package com.uaemex.airport.airline;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/airlines")
public class AirlineController {
    private final AirlineService airlineService;

    @GetMapping(path = "/{airlineId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Airline getAirline(@PathVariable("airlineId") UUID airlineId){
        return airlineService.getAirline(airlineId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<Airline> getAirlines(){
        return airlineService.getAirlines();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void createNewAirline(@RequestBody Airline airline){
        airlineService.addNewAirline(airline);
    }

    @PutMapping(path = "{airlineId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void updateAirline(
            @PathVariable("airlineId") UUID airlineId,
            @RequestParam String name){
        airlineService.updateAirline(airlineId, name);
    }

    @DeleteMapping(path = "{airlineId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteAirline(
            @PathVariable("airlineId") UUID airlineId){
        airlineService.deleteAirline(airlineId);
    }
}
