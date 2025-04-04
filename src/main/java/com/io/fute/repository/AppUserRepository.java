package com.io.fute.repository;

import com.io.fute.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    UserDetails findByUsername(String username);
}
