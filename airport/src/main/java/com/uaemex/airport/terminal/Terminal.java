package com.uaemex.airport.terminal;

import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.route.Route;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "terminal")
public class Terminal {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "tinyint")
    private int type;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "terminal_id")
    private List<BoardingRoom> boardingRooms;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "airline_id")
    private List<Route> routes;
}
