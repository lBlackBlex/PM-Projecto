package com.uaemex.airport.plane;

import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import com.uaemex.airport.user.User;
import com.uaemex.airport.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PlaneService {
    private final PlaneRepository planeRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;

    public Plane getPlane(UUID planeId) {
        Optional<Plane> planeOptional = planeRepository.findById(planeId);
        if (planeOptional.isEmpty())
            throw new IllegalStateException("Plane with id " + planeId + "does not exist");
        return planeOptional.get();
    }

    public List<Plane> getPlanes(){
        return planeRepository.findAll();
    }


    public void addNewPlane(Plane plane){
        //TODO Algo de logica para diferenciar aviones
        planeRepository.save(plane);
    }

    //TODO modificar FK's (routes, user)
    @Transactional
    public void updatePlane(UUID planeId, Integer capacity, String model, String aircraft_code){
        Plane plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new IllegalStateException("Plane with id " + planeId + " does not exist"));

        if (capacity != null && capacity > 0 && !Objects.equals(plane.getCapacity(), capacity))
            plane.setCapacity(capacity);

        if (model != null && model.length() > 0 && !Objects.equals(plane.getModel(), model))
            plane.setModel(model);

        if(aircraft_code != null && aircraft_code.length() > 0 && !Objects.equals(plane.getAircraft_code(), aircraft_code))
            plane.setAircraft_code(aircraft_code);
    }

    @Transactional
    public void addPlaneRoute(UUID planeId, UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + " does not exist"));
        Plane plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new IllegalStateException("Plane with id " + planeId + " does not exist"));

        route.setPlane(plane);
        plane.getRoutes().add(route);
    }

    @Transactional
    public void removePlaneRoute(UUID planeId, UUID routeId){
        Plane plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new IllegalStateException("Plane with id " + planeId + " does not exist"));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + " does not exist"));

        plane.getRoutes().remove(route);
    }

    public void deletePlane(UUID planeId){
        Plane plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new IllegalStateException("Plane with id " + planeId + " does not exist"));

        List<User> pilots = userRepository.findAllByPlanesContains(plane);
        pilots.forEach(pilot -> pilot.getPlanes().remove(plane));
        planeRepository.deleteById(planeId);
    }
}
