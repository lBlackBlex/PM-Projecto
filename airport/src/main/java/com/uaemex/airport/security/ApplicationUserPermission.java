package com.uaemex.airport.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationUserPermission {
    AIRLINE_READ("airline:read"),
    AIRLINE_WRITE("airline:write"),
    BOARDING_ROOM_READ("boarding_room:read"),
    BOARDING_ROOM_WRITE("boarding_room:write"),
    PLANE_READ("plane:read"),
    PLANE_WRITE("plane:write"),
    ROUTE_READ("route:read"),
    ROUTE_WRITE("route:write"),
    TERMINAL_READ("terminal:read"),
    TERMINAL_WRITE("terminal:write"),
    TICKET_READ("ticket:read"),
    TICKET_WRITE("ticket:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;
}
