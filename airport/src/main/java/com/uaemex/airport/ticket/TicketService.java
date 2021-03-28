package com.uaemex.airport.ticket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public Ticket getTicket(UUID ticketId){
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isEmpty())
            throw new IllegalStateException("Ticket with id " + ticketId + "does not exist");
        return ticketOptional.get();
    }

    public List<Ticket> getTickets(){
        return ticketRepository.findAll();
    }

    public void addNewTicket(Ticket ticket){
        //TODO Logica para boletos
        ticketRepository.save(ticket);
    }

    //TODO Modificar FK's (ruta, usuario)
    @Transactional
    public void updateTicket(UUID ticketId, boolean check_in, String seat, boolean resale){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalStateException("Ticket with id " + ticketId + " does not exist"));

        if (!ticket.isCheck_in()) ticket.setCheck_in(check_in);
        if (!ticket.isResale()) ticket.setResale(resale);
        if (seat != null && seat.length() > 0 && !Objects.equals(ticket.getSeat(), seat))
            ticket.setSeat(seat);
    }

    public void deleteTicket(UUID ticketId){
        boolean exists = ticketRepository.existsById(ticketId);
        if (!exists) throw new IllegalStateException("Ticket with id " + ticketId + " does not exist");
        ticketRepository.deleteById(ticketId);
    }
}
