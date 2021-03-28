package com.uaemex.airport.boardingRoom;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/boarding_rooms")
public class BoardingRoomController {
    private final BoardingRoomService boardingRoomService;

    @GetMapping(path = "/{boardingRoomId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public BoardingRoom getBoardingRoom(@PathVariable("boardingRoomId") UUID boardingRoomId){
        return boardingRoomService.getBoardingRoom(boardingRoomId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<BoardingRoom> getBoardingRooms(){
        return boardingRoomService.getBoardingRooms();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void createBoardingRoom(@RequestBody BoardingRoom boardingRoom){
        boardingRoomService.createBoardingRoom(boardingRoom);
    }

    @PutMapping(path = "{boardingRoomId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void updateBoardingRoom(
            @PathVariable("boardingRoomId") UUID boardingRoomId,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) String name){
        boardingRoomService.updateBoardingRoom(boardingRoomId, capacity, name);
    }
    @DeleteMapping(path = "{boardingRoomId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteBoardingRoom(
            @PathVariable("boardingRoomId") UUID boardingRoomId){
        boardingRoomService.deleteBoardingRoom(boardingRoomId);
    }
}
