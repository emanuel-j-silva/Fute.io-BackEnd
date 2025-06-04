package com.io.fute.controller;

import com.io.fute.dto.user.UserInfo;
import com.io.fute.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users/me")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserInfo> getUserInfo(
            @AuthenticationPrincipal(expression = "id")UUID userId){

        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchUserInfo(userId));
    }
}
