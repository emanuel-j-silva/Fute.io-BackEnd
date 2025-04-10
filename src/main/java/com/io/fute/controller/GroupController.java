package com.io.fute.controller;

import com.io.fute.dto.group.GroupInfo;
import com.io.fute.dto.group.GroupRequest;
import com.io.fute.dto.response.ResponseDTO;
import com.io.fute.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createGroup(@RequestBody @Valid GroupRequest request,
                                              @AuthenticationPrincipal(expression = "id") UUID userId){

        groupService.createGroup(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("Grupo criado com sucesso"));
    }

    @GetMapping
    public ResponseEntity<List<GroupInfo>> fetchGroupsByUser(@AuthenticationPrincipal(expression = "id") UUID userId){
        List<GroupInfo> groups = groupService.fetchAllGroupsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(groups);
    }
}
