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

    @PutMapping(path = "{planeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void updatePlane(
            @PathVariable("planeId") UUID planeId,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String model){
        planeService.updatePlane(planeId, capacity, model);
    }

    @DeleteMapping(path = "{planeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deletePlane(
            @PathVariable("planeId") UUID planeId){
        planeService.deletePlane(planeId);
    }
}
