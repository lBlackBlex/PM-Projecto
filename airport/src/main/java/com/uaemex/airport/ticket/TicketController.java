package com.uaemex.airport.ticket;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/tickets")
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public List<Ticket> getTickets(){
        return ticketService.getTickets();
    }

    @PostMapping
    public void registerNewTicket(@RequestBody Ticket ticket){
        ticketService.addNewTicket(ticket);
    }

    @PutMapping(path = "{ticketId}")
    public void updateTicket(
            @PathVariable("ticketId") UUID ticketId,
            @RequestParam(required = false) boolean check_in,
            @RequestParam(required = false) String seat,
            @RequestParam(required = false) boolean resale){
        ticketService.updateTicket(ticketId, check_in, seat, resale);
    }

    @DeleteMapping(path = "{ticketId}")
    public void deleteTicket(@PathVariable("ticketId") UUID ticketId){
        ticketService.deleteTicket(ticketId);
    }
}
