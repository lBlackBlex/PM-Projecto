package com.uaemex.airport.user;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER', 'ROLE_PILOT')")
    public User getUser(@PathVariable("userId") UUID userId){
        return userService.getUser(userId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void registerNewUser(
            @RequestBody User user) {
        userService.createNewUser(user);
    }

    @PutMapping(path = "{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_PILOT')")
    public void updateUser(
            @PathVariable("userId") UUID userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String last_name) {
        userService.updateUser(userId, email, name, last_name);
    }

    @DeleteMapping(path = "{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteUser(
            @PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
    }
}
