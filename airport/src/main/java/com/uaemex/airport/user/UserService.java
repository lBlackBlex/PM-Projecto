package com.uaemex.airport.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUser(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new IllegalStateException("User with id " + userId + "does not exist");
        return optionalUser.get();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void createNewUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

        if (userOptional.isPresent()) throw new IllegalStateException("Email already in use");
        //TODO mejor implementacion
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //TODO Modificar password
    @Transactional
    public void updateUser(UUID userId, String email, String name, String last_name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isPresent()) throw new IllegalStateException("Email already in use");
            user.setEmail(email);
        }

        if (name != null && name.length() > 0 && !Objects.equals(user.getName(), name)) user.setName(name);

        if (last_name != null && last_name.length() > 0 && !Objects.equals(user.getLast_name(), last_name))
            user.setLast_name(last_name);
    }

    public void deleteUser(UUID userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) throw new IllegalStateException("User with id " + userId + " does not exist");
        userRepository.deleteById(userId);
    }
}
