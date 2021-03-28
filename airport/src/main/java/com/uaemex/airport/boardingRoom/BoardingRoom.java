package com.uaemex.airport.boardingRoom;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.terminal.Terminal;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "boarding_room")
public class BoardingRoom {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "smallint")
    private int capacity;
    @Column(nullable = false, length = 45)
    private String name;
    @ManyToOne()
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "boarding_room_route",
            joinColumns = {@JoinColumn(name = "boarding_room_id")},
            inverseJoinColumns = {@JoinColumn(name = "route_id")}
    )
    private Set<Route> routes;
}
