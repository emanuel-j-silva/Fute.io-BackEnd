package com.io.fute.repository;

import com.io.fute.entity.user.AppUser;
import com.io.fute.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    boolean existsByNameAndUser(String name, AppUser user);
    boolean existsByNameAndUserAndIdNot(String name, AppUser user, UUID id);
    List<Group> findAllByUserId(UUID userId);

}
