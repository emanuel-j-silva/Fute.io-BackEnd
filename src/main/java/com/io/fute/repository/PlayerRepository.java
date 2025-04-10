package com.io.fute.repository;

import com.io.fute.entity.AppUser;
import com.io.fute.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByNameAndUser(String name, AppUser user);
    List<Player> findAllByUserId(UUID userId);

}
