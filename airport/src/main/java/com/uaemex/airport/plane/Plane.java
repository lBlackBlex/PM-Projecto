package com.uaemex.airport.plane;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table
public class Plane {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private int capacity;
    @Column(nullable = false, length = 45)
    private String model;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "plane_id")
    private List<Route> routes;
    @ManyToMany(mappedBy = "planes")
    private Set<User> users;
}
