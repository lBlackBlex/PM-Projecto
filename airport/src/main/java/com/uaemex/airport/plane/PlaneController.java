package com.uaemex.airport.plane;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/planes")
public class PlaneController {
    private final PlaneService planeService;

    @GetMapping(path = "/{planeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Plane getPlane(@PathVariable("planeId") UUID planeId){
        return planeService.getPlane(planeId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<Plane> getPlanes(){
        return planeService.getPlanes();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void registerNewPlane(
            @RequestBody Plane plane){
        planeService.addNewPlane(plane);
    }

    @PostMapping(path = "{planeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void addPlaneRoute(
            @PathVariable("planeId") UUID planeId,
            @RequestParam UUID routeId){
        planeService.addPlaneRoute(planeId, routeId);
    }

    @PutMapping(path = "{planeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void updatePlane(
            @PathVariable("planeId") UUID planeId,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String aircraft_code){
        planeService.updatePlane(planeId, capacity, model, aircraft_code);
    }

    @DeleteMapping(path = "{planeId}/{routeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void removePlaneRoute(
            @PathVariable("planeId") UUID planeId,
            @PathVariable("routeId") UUID routeId){
        planeService.removePlaneRoute(planeId, routeId);
    }

    @DeleteMapping(path = "{planeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deletePlane(
            @PathVariable("planeId") UUID planeId){
        planeService.deletePlane(planeId);
    }
}
