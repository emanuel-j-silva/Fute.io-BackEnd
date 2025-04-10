package com.io.fute.service;

import com.io.fute.dto.player.PlayerInfo;
import com.io.fute.dto.player.PlayerRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.entity.Player;
import com.io.fute.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void createPlayer(PlayerRequest playerRequest, AppUser user){
        String errors = "";
        if (playerRepository.existsByNameAndUser(playerRequest.name(), user)){
            errors += "Já existe um jogador com este nome\n";
        }
        if (playerRequest.overall() > 100 || playerRequest.overall() < 1){
            errors += "Overall fora do limite";
        }
        if (!errors.isEmpty()) throw new IllegalArgumentException(errors);

        Player savedPlayer = new Player(playerRequest.name(), playerRequest.overall(), user);
        playerRepository.save(savedPlayer);
    }

    public List<PlayerInfo> fetchAllPlayersByUser(UUID userId){
        return playerRepository.findAllByUserId(userId).stream()
                .map(player -> new PlayerInfo(player.getName(),player.getOverall(), player.getUrlPhoto()))
                .toList();
    }
}
