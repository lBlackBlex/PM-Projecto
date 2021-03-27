package com.uaemex.airport.terminal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TerminalService {
    private final TerminalRepository terminalRepository;

    public List<Terminal> getTerminals(){
        return terminalRepository.findAll();
    }

    public void addNewTerminal(Terminal terminal){
        Optional<Terminal> terminalOptional = terminalRepository.findTerminalByType(terminal.getType());

        if (terminalOptional.isPresent()) throw new IllegalStateException("Terminal type already in use");
        terminalRepository.save(terminal);
    }

    public void deleteTerminal(UUID terminalId){
        boolean exists = terminalRepository.existsById(terminalId);
        if (!exists) throw new IllegalStateException("Terminal with id " + terminalId + " does not exist");
        terminalRepository.deleteById(terminalId);
    }

}
