package com.io.fute.service;

import com.io.fute.dto.GroupRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.entity.Group;
import com.io.fute.repository.AppUserRepository;
import com.io.fute.repository.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    private GroupRepository groupRepository;
    private AppUserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, AppUserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public void createGroup(GroupRequest group){
        AppUser user = userRepository.findById(group.userId())
                .orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado"));

        if (groupRepository.existsByNameAndUser(group.name(), user)){
            throw new IllegalArgumentException("Você já possui um grupo com esse nome");
        }

        Group savedGroup = new Group(group.name(),group.location(),user);
        groupRepository.save(savedGroup);
    }

    public List<Group> fetchAllGroupsByUser(UUID userId){
        return groupRepository.findAllByUserId(userId);
    }
}
