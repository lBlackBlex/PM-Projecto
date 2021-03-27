package com.uaemex.airport.boardingRoom;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/boarding_rooms")
public class BoardingRoomController {
    private final BoardingRoomService boardingRoomService;

    @GetMapping
    public List<BoardingRoom> getBoardingRooms(){
        return boardingRoomService.getBoardingRooms();
    }

    @PostMapping
    public void createBoardingRoom(@RequestBody BoardingRoom boardingRoom){
        boardingRoomService.createBoardingRoom(boardingRoom);
    }

    @PutMapping(path = "{boardingRoomId}")
    public void updateBoardingRoom(
            @PathVariable("boardingRoomId") UUID boardingRoomId,
            @RequestParam(required = false) int capacity,
            @RequestParam(required = false) String name){
        boardingRoomService.updateBoardingRoom(boardingRoomId, capacity, name);
    }
    @DeleteMapping(path = "{boardingRoomId}")
    public void deleteBoardingRoom(
            @PathVariable("boardingRoomId") UUID boardingRoomId){
        boardingRoomService.deleteBoardingRoom(boardingRoomId);
    }
}
