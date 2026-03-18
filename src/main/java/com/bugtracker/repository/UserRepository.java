package com.bugtracker.repository;

import com.bugtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {         // <User, Long> arata doar la care tabela ne referim si Long e tipul id-ului
}