package com.uaemex.airport.ticket;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping(path = "/{ticketId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER', 'ROLE_PILOT')")
    public Ticket getTicket(@PathVariable("ticketId") UUID ticketId){
        return ticketService.getTicket(ticketId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<Ticket> getTickets(){
        return ticketService.getTickets();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER', 'ROLE_PILOT')")
    public Ticket registerNewTicket(@RequestBody Ticket ticket){
        return ticketService.addNewTicket(ticket);
    }

    @PostMapping(path = "{ticketId}")
    public void addTicketUser(
            @PathVariable("ticketId") UUID ticketId,
            @RequestParam UUID routeId,
            @RequestParam UUID userId){
        ticketService.addTicketUser(ticketId, routeId, userId);
    }

    @PutMapping(path = "{ticketId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public void updateTicket(
            @PathVariable("ticketId") UUID ticketId,
            @RequestParam(required = false) boolean check_in,
            @RequestParam(required = false) String seat,
            @RequestParam(required = false) boolean resale){
        ticketService.updateTicket(ticketId, check_in, seat, resale);
    }

    @DeleteMapping(path = "{ticketId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteTicket(@PathVariable("ticketId") UUID ticketId){
        ticketService.deleteTicket(ticketId);
    }
}
