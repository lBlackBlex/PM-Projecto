package com.uaemex.airport.plane;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/planes")
public class PlaneController {
    private final PlaneService planeService;

    @GetMapping
    public List<Plane> getPlanes(){
        return planeService.getPlanes();
    }

    @PostMapping
    public void registerNewPlane(
            @RequestBody Plane plane){
        planeService.addNewPlane(plane);
    }

    @PutMapping(path = "{planeId}")
    public void updatePlane(
            @PathVariable("planeId") UUID planeId,
            @RequestParam(required = false) int capacity,
            @RequestParam(required = false) String model){
        planeService.updatePlane(planeId, capacity, model);
    }

    @DeleteMapping(path = "{planeId}")
    public void deletePlane(
            @PathVariable("planeId") UUID planeId){
        planeService.deletePlane(planeId);
    }
}
