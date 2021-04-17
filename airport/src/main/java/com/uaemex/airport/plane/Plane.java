package com.uaemex.airport.plane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uaemex.airport.route.Route;
import com.uaemex.airport.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "plane_aircraft_code_unique", columnNames = "aircraft_code")
        }
)
public class Plane {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private int capacity;
    @Column(nullable = false, length = 45)
    private String model;
    @Column(nullable = false, length = 15)
    private String aircraft_code;
    @JsonIgnoreProperties("plane")
    @OneToMany
    @JoinColumn(name = "plane_id")
    private Set<Route> routes = new HashSet<>();
    @JsonIgnoreProperties("plane")
    @ManyToMany(mappedBy = "planes")
    private Set<User> pilots;

    @Override
    public int hashCode() {
        return Objects.hash(id, capacity, model, aircraft_code);
    }
}
