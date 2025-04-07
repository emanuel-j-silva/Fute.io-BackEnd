package com.io.fute.repository;

import com.io.fute.entity.AppUser;
import com.io.fute.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    boolean existsByNameAndUser(String name, AppUser user);
    boolean existsByNameAndUserAndIdNot(String name, AppUser user, UUID id);

}
