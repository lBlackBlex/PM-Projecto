package com.uaemex.airport.terminal;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/terminals")
public class TerminalController {
    private final TerminalService terminalService;

    @GetMapping
    public List<Terminal> getTerminal(){
        return terminalService.getTerminals();
    }

    @PostMapping
    public void registerNewUser(
            @RequestBody Terminal terminal){
        terminalService.addNewTerminal(terminal);
    }

    @DeleteMapping(path = "{terminalId}")
    public void deleteTerminal(
            @PathVariable("terminalId") UUID terminalId){
        terminalService.deleteTerminal(terminalId);
    }
}
