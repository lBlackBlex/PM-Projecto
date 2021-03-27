package com.uaemex.airport.plane;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PlaneService {
    private final PlaneRepository planeRepository;

    public List<Plane> getPlanes(){
        return planeRepository.findAll();
    }

    public void addNewPlane(Plane plane){
        //TODO Algo de logica para diferenciar aviones
        planeRepository.save(plane);
    }

    @Transactional
    public void updatePlane(UUID planeId, int capacity, String model){
        Plane plane = planeRepository.findById(planeId)
                .orElseThrow(() -> new IllegalStateException("Plane with id " + planeId + " does not exist"));

        if (capacity > 0 && !Objects.equals(plane.getCapacity(), capacity))
            plane.setCapacity(capacity);

        if (model != null && model.length() > 0 && !Objects.equals(plane.getModel(), model))
            plane.setModel(model);
    }

    public void deletePlane(UUID planeId){
        boolean exists = planeRepository.existsById(planeId);
        if (!exists) throw new IllegalStateException("Plane with id " + planeId + " does not exist");
        planeRepository.deleteById(planeId);
    }
}
