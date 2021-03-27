package com.uaemex.airport.airline;

import com.uaemex.airport.route.Route;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "airline",
        uniqueConstraints = {
                @UniqueConstraint(name = "airline_unique_name", columnNames = "name")
        }
)
public class Airline {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, length = 45)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "airline_id")
    private List<Route> routes;
}
