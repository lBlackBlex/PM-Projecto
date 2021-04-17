package com.uaemex.airport.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uaemex.airport.route.Route;
import com.uaemex.airport.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "ticket",
        uniqueConstraints = {
                @UniqueConstraint(name = "ticket_seat_unique", columnNames = "seat")
        }
)
public class Ticket {

    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean check_in = false;
    @Column(nullable = false, columnDefinition = "char(5)")
    private String seat;
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean resale = false;
    @JsonIgnoreProperties("tickets")
    @ManyToOne()
    @JoinColumn(name = "route_id")
    private Route route;
    @JsonIgnoreProperties("tickets")
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
