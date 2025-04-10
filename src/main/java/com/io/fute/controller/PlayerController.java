package com.io.fute.controller;

import com.io.fute.dto.player.PlayerInfo;
import com.io.fute.dto.player.PlayerRequest;
import com.io.fute.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<String> savePlayer(@RequestBody @Valid PlayerRequest request,
                                        @AuthenticationPrincipal(expression = "id")UUID userId){

        playerService.createPlayer(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Jogador adicionado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<PlayerInfo>> fetchPlayersByUser(@AuthenticationPrincipal(expression = "id") UUID userId){
        List<PlayerInfo> players = playerService.fetchAllPlayersByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }
}
