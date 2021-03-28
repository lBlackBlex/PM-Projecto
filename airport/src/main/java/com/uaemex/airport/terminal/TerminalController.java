package com.uaemex.airport.terminal;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/terminals")
public class TerminalController {
    private final TerminalService terminalService;

    @GetMapping(path = "/{terminalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public Terminal getTerminal(@PathVariable("terminalId") UUID terminalId){
        return terminalService.getTerminal(terminalId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<Terminal> getTerminal(){
        return terminalService.getTerminals();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void registerNewUser(
            @RequestBody Terminal terminal){
        terminalService.addNewTerminal(terminal);
    }

    @DeleteMapping(path = "{terminalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteTerminal(
            @PathVariable("terminalId") UUID terminalId){
        terminalService.deleteTerminal(terminalId);
    }
}
