package com.uaemex.airport.boardingRoom;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BoardingRoomService {
    private final BoardingRoomRepository boardingRoomRepository;

    public List<BoardingRoom> getBoardingRooms(){
        return boardingRoomRepository.findAll();
    }

    public void createBoardingRoom(BoardingRoom boardingRoom){
        Optional<BoardingRoom> boardingRoomByName = boardingRoomRepository.findBoardingRoomByName(boardingRoom.getName());
        if (boardingRoomByName.isPresent()) throw new IllegalStateException("Name already in use");
        boardingRoomRepository.save(boardingRoom);
    }

    //TODO Modificar FK Terminal
    @Transactional
    public void updateBoardingRoom(UUID boardingRoomId, int capacity, String name){
        BoardingRoom boardingRoom = boardingRoomRepository.findById(boardingRoomId)
                .orElseThrow(() -> new IllegalStateException("Boarding room with id " + boardingRoomId + " does not exist"));

        if(capacity > 0 && !Objects.equals(boardingRoom.getCapacity(), capacity)) boardingRoom.setCapacity(capacity);
        if(name != null && name.length() > 0 && !Objects.equals(boardingRoom.getName(), name))
            boardingRoom.setName(name);

    }

    public void deleteBoardingRoom(UUID boardingRoomId){
        boolean exists = boardingRoomRepository.existsById(boardingRoomId);
        if (!exists) throw new IllegalStateException("Boarding room with id " + boardingRoomId + " does not exist");
        boardingRoomRepository.deleteById(boardingRoomId);
    }
}
