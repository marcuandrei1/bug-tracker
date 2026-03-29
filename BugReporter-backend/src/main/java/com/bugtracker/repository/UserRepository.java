package com.bugtracker.repository;

import com.bugtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {         // <User, Long> arata doar la care tabela ne referim si Long e tipul id-ului

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}