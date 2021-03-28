package com.uaemex.airport.boardingRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoardingRoomRepository extends JpaRepository<BoardingRoom, UUID> {
    Optional<BoardingRoom> findBoardingRoomByName(String name);
}
