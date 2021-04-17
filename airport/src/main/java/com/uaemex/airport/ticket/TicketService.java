package com.uaemex.airport.ticket;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import com.uaemex.airport.user.User;
import com.uaemex.airport.user.UserRepository;
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
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;

    public Ticket getTicket(UUID ticketId){
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (ticketOptional.isEmpty())
            throw new IllegalStateException("Ticket with id " + ticketId + "does not exist");
        return ticketOptional.get();
    }

    public List<Ticket> getTickets(){
        return ticketRepository.findAll();
    }

    public Ticket addNewTicket(Ticket ticket){
        //TODO Logica para boletos
        ticketRepository.save(ticket);
        return ticket;
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

    @Transactional
    public void addTicketUser(UUID ticketId, UUID routeId, UUID userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalStateException("Ticket with id " + ticketId + " does not exist"));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalStateException("Route with id " + routeId + "does not exist"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        ticket.setRoute(route);
        user.getTickets().add(ticket);
        route.getTickets().add(ticket);
    }

    public void deleteTicket(UUID ticketId){
        boolean exists = ticketRepository.existsById(ticketId);
        if (!exists) throw new IllegalStateException("Ticket with id " + ticketId + " does not exist");
        ticketRepository.deleteById(ticketId);
    }
}
