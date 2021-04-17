package com.uaemex.airport.terminal;

import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.boardingRoom.BoardingRoomRepository;
import com.uaemex.airport.route.Route;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TerminalService {
    private final TerminalRepository terminalRepository;
    private final BoardingRoomRepository boardingRoomRepository;

    public Terminal getTerminal(UUID terminalId){
        Optional<Terminal> optionalTerminal = terminalRepository.findById(terminalId);
        if (optionalTerminal.isEmpty())
            throw new IllegalStateException("Terminal with id " + terminalId + "does not exist");
        return optionalTerminal.get();
    }

    public List<Terminal> getTerminals(){
        return terminalRepository.findAll();
    }

    public void addNewTerminal(Terminal terminal){
        Optional<Terminal> terminalOptional = terminalRepository.findTerminalByType(terminal.getType());

        if (terminalOptional.isPresent()) throw new IllegalStateException("Terminal type already in use");
        terminalRepository.save(terminal);
    }

    @Transactional
    public void addTerminalBoardingRoom(UUID terminalId, UUID boardingRoomId) {
        Terminal terminal = terminalRepository.findById(terminalId)
                .orElseThrow(() -> new IllegalStateException("Terminal with id " + terminalId + " does not exist"));
        BoardingRoom boardingRoom = boardingRoomRepository.findById(boardingRoomId)
                .orElseThrow(() -> new IllegalStateException("Boarding room with id " + boardingRoomId + " does not exist"));

        terminal.getBoardingRooms().add(boardingRoom);
    }

    @Transactional
    public void removeTerminalBoardingRoom(UUID terminalId, UUID boardingRoomId){
        Terminal terminal = terminalRepository.findById(terminalId)
                .orElseThrow(() -> new IllegalStateException("Terminal with id " + terminalId + " does not exist"));
        BoardingRoom boardingRoom = boardingRoomRepository.findById(boardingRoomId)
                .orElseThrow(() -> new IllegalStateException("Boarding room with id " + boardingRoomId + "does not exist"));

        terminal.getBoardingRooms().remove(boardingRoom);
    }

    public void deleteTerminal(UUID terminalId){
        boolean exists = terminalRepository.existsById(terminalId);
        if (!exists) throw new IllegalStateException("Terminal with id " + terminalId + " does not exist");
        terminalRepository.deleteById(terminalId);
    }
}
