package com.uaemex.airport.auth;

import com.uaemex.airport.user.User;
import com.uaemex.airport.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.uaemex.airport.security.ApplicationUserRole.*;

@AllArgsConstructor
@Service
public class ApplicationUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("User with email " + email + " does not exist");
        //TODO mejor implementacion
        if (user.get().getRole().equals("ADMIN")){
            return new ApplicationUser(
                    ADMIN.getGrantedAuthorities(),
                    user.get().getPassword(),
                    user.get().getEmail(),
                    true,
                    true,
                    true,
                    true);
        }
        if (user.get().getRole().equals("EMPLOYEE")){
            return new ApplicationUser(
                    EMPLOYEE.getGrantedAuthorities(),
                    user.get().getPassword(),
                    user.get().getEmail(),
                    true,
                    true,
                    true,
                    true);
        }if (user.get().getRole().equals("PILOT")){
            return new ApplicationUser(
                    PILOT.getGrantedAuthorities(),
                    user.get().getPassword(),
                    user.get().getEmail(),
                    true,
                    true,
                    true,
                    true);
        }        if (user.get().getRole().equals("USER")){
            return new ApplicationUser(
                    USER.getGrantedAuthorities(),
                    user.get().getPassword(),
                    user.get().getEmail(),
                    true,
                    true,
                    true,
                    true);
        }
        return null;
    }
}
