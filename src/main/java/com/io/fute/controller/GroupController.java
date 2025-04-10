package com.io.fute.controller;

import com.io.fute.dto.group.GroupInfo;
import com.io.fute.dto.group.GroupRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody @Valid GroupRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();

        groupService.createGroup(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Grupo criado com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<GroupInfo>> fetchGroupsByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();

        List<GroupInfo> groups = groupService.fetchAllGroupsByUser(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }
}
