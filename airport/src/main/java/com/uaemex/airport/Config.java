package com.uaemex.airport;

import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.airline.AirlineRepository;
import com.uaemex.airport.auth.ApplicationUserService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
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

            Airline airline3 = new Airline();
            airline3.setName("airline3");

            Airline airline4 = new Airline();
            airline4.setName("airline4");

            airlineRepository.saveAll(List.of(airline,airline2,airline3,airline4));

            BoardingRoom boardingRoom = new BoardingRoom();
            boardingRoom.setCapacity(200);
            boardingRoom.setName("boardingRoom1");

            BoardingRoom boardingRoom2 = new BoardingRoom();
            boardingRoom2.setCapacity(300);
            boardingRoom2.setName("boardingRoom2");

            boardingRoomRepository.saveAll(List.of(boardingRoom, boardingRoom2));

            Terminal terminal = new Terminal();
            terminal.setType(1);
            terminal.getBoardingRooms().add(boardingRoom);
            terminal.getBoardingRooms().add(boardingRoom2);
            Terminal terminal2 = new Terminal();
            terminal2.setType(2);

            terminalRepository.saveAll(List.of(terminal ,terminal2));

            Route route = new Route();
            route.setDate(new java.sql.Date(System.currentTimeMillis()));
            route.setFrom("México");
            route.setTo("Miami");
            route.setEtd(Timestamp.valueOf("2021-04-27 04:00:00"));
            route.setEta(Timestamp.valueOf("2021-04-27 06:00:00"));
            route.setCost(700);
            route.getAirlines().add(airline);

            Route route3 = new Route();
            route3.setDate(new java.sql.Date(System.currentTimeMillis()));
            route3.setFrom("México");
            route3.setTo("Miami");
            route3.setEtd(Timestamp.valueOf("2021-04-27 04:00:00"));
            route3.setEta(Timestamp.valueOf("2021-04-27 06:00:00"));
            route3.setCost(900);
            route3.getAirlines().add(airline);
            route3.getAirlines().add(airline2);

            Route route2 = new Route();
            route2.setDate(new java.sql.Date(System.currentTimeMillis()));
            route2.setFrom("España");
            route2.setTo("Miami");
            route2.setEtd(Timestamp.valueOf("2021-04-27 04:00:00"));
            route2.setEta(Timestamp.valueOf("2021-04-28 04:00:00"));
            route2.setCost(700);
            route2.getAirlines().add(airline3);

            routeRepository.saveAll(List.of(route, route2, route3));

            Plane plane = new Plane();
            plane.setCapacity(100);
            plane.setModel("AAA");
            plane.setAircraft_code("XA-KEL");
            plane.getRoutes().add(route);
            plane.getRoutes().add(route3);

            Plane plane2 = new Plane();
            plane2.setCapacity(200);
            plane2.setModel("BBB");
            plane2.setAircraft_code("XA-KEM");
            plane2.getRoutes().add(route2);

            planeRepository.saveAll(List.of(plane, plane2));

            User user1 = new User();
            user1.setEmail("admin");
//            user1.setEmail("admin@admin.com");
            user1.setPassword(new BCryptPasswordEncoder(10).encode("q"));
            user1.setName("admin");
            user1.setLast_name("adminlast");
            user1.setRole("ADMIN");

            User user2 = new User();
            user2.setEmail("employee@user.com");
            user2.setPassword(new BCryptPasswordEncoder(10).encode("q"));
            user2.setName("employee");
            user2.setLast_name("employeelast");
            user2.setRole("EMPLOYEE");

            User user3 = new User();
            user3.setEmail("pilot@user.com");
            user3.setPassword(new BCryptPasswordEncoder(10).encode("q"));
            user3.setName("pilot");
            user3.setLast_name("pilotlast");
            user3.setRole("PILOT");
            user3.getPlanes().add(plane);
            user3.getPlanes().add(plane2);

            User user5 = new User();
            user5.setEmail("pilot2@user.com");
            user5.setPassword(new BCryptPasswordEncoder(10).encode("q"));
            user5.setName("pilot2");
            user5.setLast_name("pilot2last");
            user5.setRole("PILOT");
            user5.getPlanes().add(plane);
            
            User user6 = new User();
            user6.setEmail("pilot3@user.com");
            user6.setPassword(new BCryptPasswordEncoder(10).encode("q"));
            user6.setName("pilot3");
            user6.setLast_name("pilot3last");
            user6.setRole("PILOT");

            User user4 = new User();
            user4.setEmail("user@user.com");
            user4.setPassword(new BCryptPasswordEncoder(10).encode("q"));
            user4.setName("user");
            user4.setLast_name("testlast");

            userRepository.saveAll(List.of(user1, user2, user3,user4, user5, user6));

            Ticket ticket = new Ticket();
            ticket.setSeat("ABC1");
            ticket.setUser(user4);
            ticketRepository.save(ticket);
        };
    }
}
