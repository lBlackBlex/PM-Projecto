package com.uaemex.airport.terminal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.route.Route;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

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
    @JsonIgnoreProperties("terminal")
    @OneToMany
    @JoinColumn(name = "terminal_id")
    private List<BoardingRoom> boardingRooms = new ArrayList<>();
}
