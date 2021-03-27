package com.uaemex.airport.route;

import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.plane.Plane;
import com.uaemex.airport.ticket.Ticket;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, length = 45, name = "place_of_departure")
    private String from;
    @Column(nullable = false, length = 45, name = "place_of_arrival")
    private String to;
    @Transient
    private Timestamp flight_time;
    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp etd;
    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp eta;
    @Column(nullable = false)
    private float cost;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "route_id")
    private List<Ticket> tickets;
    @ManyToMany(mappedBy = "routes")
    private Set<BoardingRoom> boardingRooms;
    @ManyToOne()
    @JoinColumn(name = "plane_id")
    private Plane plane;

    /*public Timestamp getFlight_time() {
        Duration seconds = Duration.between(this.etd.toLocalDateTime(), this.eta.toLocalDateTime());
        Timestamp timestamp = new Timestamp(seconds.toMillis());
        return timestamp;
    }*/
}
