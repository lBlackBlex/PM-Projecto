package com.uaemex.airport.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.uaemex.airport.security.ApplicationUserPermission.*;

@AllArgsConstructor
@Getter
public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(
            AIRLINE_READ,
            AIRLINE_WRITE,
            BOARDING_ROOM_READ,
            BOARDING_ROOM_WRITE,
            PLANE_READ,
            PLANE_WRITE,
            ROUTE_READ,
            ROUTE_WRITE,
            TERMINAL_READ,
            TERMINAL_WRITE,
            TICKET_READ,
            TICKET_WRITE,
            USER_READ,
            USER_WRITE
    )),
    EMPLOYEE(Sets.newHashSet(
            AIRLINE_READ,
            BOARDING_ROOM_READ,
            PLANE_READ,
            ROUTE_READ,
            TERMINAL_READ,
            TICKET_READ,
            TICKET_WRITE,
            USER_READ
    )),
    PILOT(Sets.newHashSet(
            //AIRLINE_READ,
            //BOARDING_ROOM_READ,
            //PLANE_READ,
            ROUTE_READ,
            //TERMINAL_READ,
            TICKET_READ,
            TICKET_WRITE,
            USER_READ,
            USER_WRITE
    )),
    //TODO SOLO ROUTE?
    USER(Sets.newHashSet(
            //AIRLINE_READ,
            //BOARDING_ROOM_READ,
            //PLANE_READ,
            ROUTE_READ,
            //TERMINAL_READ,
            TICKET_READ,
            TICKET_WRITE,
            USER_READ,
            USER_WRITE
    ));

    private final Set<ApplicationUserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
