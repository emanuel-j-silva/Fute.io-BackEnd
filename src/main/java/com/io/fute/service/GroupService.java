package com.io.fute.service;

import com.io.fute.dto.group.GroupInfo;
import com.io.fute.dto.group.GroupRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.entity.Group;
import com.io.fute.repository.AppUserRepository;
import com.io.fute.repository.GroupRepository;
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

    public void createGroup(GroupRequest groupRequest, AppUser user){

        if (groupRepository.existsByNameAndUser(groupRequest.name(), user)){
            throw new IllegalArgumentException("Você já possui um grupo com esse nome");
        }

        Group savedGroup = new Group(groupRequest.name(),groupRequest.location(),user);
        groupRepository.save(savedGroup);
    }

    public List<GroupInfo> fetchAllGroupsByUser(UUID userId){
        return groupRepository.findAllByUserId(userId).stream()
                .map(group -> new GroupInfo(group.getName(),group.getLocation(), group.getNumberOfPlayers()))
                .toList();
    }
}
