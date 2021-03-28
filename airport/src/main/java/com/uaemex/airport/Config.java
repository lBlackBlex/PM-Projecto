package com.uaemex.airport;

import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.airline.AirlineRepository;
import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.boardingRoom.BoardingRoomRepository;
import com.uaemex.airport.plane.Plane;
import com.uaemex.airport.plane.PlaneRepository;
import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import com.uaemex.airport.terminal.Terminal;
import com.uaemex.airport.terminal.TerminalRepository;
import com.uaemex.airport.ticket.Ticket;
import com.uaemex.airport.ticket.TicketRepository;
import com.uaemex.airport.user.User;
import com.uaemex.airport.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

@Configuration
public class Config {

    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            AirlineRepository airlineRepository,
            TerminalRepository terminalRepository,
            PlaneRepository planeRepository,
            BoardingRoomRepository boardingRoomRepository,
            RouteRepository routeRepository,
            TicketRepository ticketRepository) {
        return args -> {
            Airline airline = new Airline();
            airline.setName("airline1");

            Airline airline2 = new Airline();
            airline2.setName("airline2");

            airlineRepository.saveAll(List.of(airline,airline2));

            Plane plane = new Plane();
            plane.setCapacity(100);
            plane.setModel("AAA");

            Plane plane2 = new Plane();
            plane2.setCapacity(200);
            plane2.setModel("BBB");

            planeRepository.saveAll(List.of(plane, plane2));

            Terminal terminal = new Terminal();
            terminal.setType(1);
            Terminal terminal2 = new Terminal();
            terminal2.setType(2);

            terminalRepository.saveAll(List.of(terminal ,terminal2));

            BoardingRoom boardingRoom = new BoardingRoom();
            boardingRoom.setCapacity(200);
            boardingRoom.setName("boardingRoom1");

            BoardingRoom boardingRoom2 = new BoardingRoom();
            boardingRoom2.setCapacity(300);
            boardingRoom2.setName("boardingRoom2");

            boardingRoomRepository.saveAll(List.of(boardingRoom, boardingRoom2));

            Route route = new Route();
            route.setFrom("México");
            route.setTo("Miami");
            route.setEtd(Timestamp.valueOf("2021-04-27 04:00:00"));
            route.setEta(Timestamp.valueOf("2021-04-27 06:00:00"));
            route.setCost(700);

            Route route2 = new Route();
            route2.setFrom("España");
            route2.setTo("Miami");
            route2.setEtd(Timestamp.valueOf("2021-04-27 04:00:00"));
            route2.setEta(Timestamp.valueOf("2021-04-28 04:00:00"));
            route2.setCost(700);

            routeRepository.saveAll(List.of(route, route2));

            Ticket ticket = new Ticket();
            ticket.setSeat("ABC1");

            ticketRepository.save(ticket);

            User user1 = new User();
            user1.setEmail("admin@admin.com");
            user1.setPassword("password");
            user1.setName("admin");
            user1.setLast_name("adminlast");
            user1.setRole("ADMIN");

            User user2 = new User();
            user2.setEmail("employee@user.com");
            user2.setPassword("password");
            user2.setName("employee");
            user2.setLast_name("employeelast");
            user2.setRole("EMPLOYEE");

            User user3 = new User();
            user3.setEmail("pilot@user.com");
            user3.setPassword("password");
            user3.setName("pilot");
            user3.setLast_name("pilotlast");
            user3.setRole("PILOT");

            User user4 = new User();
            user4.setEmail("user@user.com");
            user4.setPassword("password");
            user4.setName("user");
            user4.setLast_name("testlast");

            userRepository.saveAll(List.of(user1, user2, user3,user4));
        };
    }
}
