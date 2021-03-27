package com.uaemex.airport.terminal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TerminalRepository extends JpaRepository<Terminal, UUID> {
    Optional<Terminal> findTerminalByType(Integer type);
}
