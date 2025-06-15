package com.io.fute.service;

import com.io.fute.dto.player.PlayerInfo;
import com.io.fute.dto.player.PlayerRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.entity.Group;
import com.io.fute.entity.Player;
import com.io.fute.repository.AppUserRepository;
import com.io.fute.repository.GroupRepository;
import com.io.fute.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final AppUserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, AppUserRepository userRepository, GroupRepository groupRepository) {
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public void createPlayer(PlayerRequest playerRequest, UUID userID){
        AppUser user = userRepository.findById(userID)
                .orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado!"));

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
        return playerRepository.findAllByUserIdOrderByOverallDesc(userId).stream()
                .map(player -> new PlayerInfo(
                        player.getId(), player.getName(),player.getOverall(), player.getUrlPhoto())
                ).toList();
    }

    public List<PlayerInfo> fetchTopPlayersByUser(UUID userId){
        return playerRepository.findTop10ByUserIdOrderByOverallDesc(userId).stream()
                .map(player -> new PlayerInfo(player.getId(), player.getName(),
                        player.getOverall(), player.getUrlPhoto()))
                .toList();
    }

    public void removePlayer(Long playerId, UUID userId){
        Player player = playerRepository.findById(playerId)
                .orElseThrow(()-> new EntityNotFoundException("Jogador não encontrado"));

        if (!player.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Você não tem permissão para acessar esse jogador");
        }

        List<Group> userGroups = groupRepository.findAllByUserId(userId);
        for(Group group: userGroups){
            if(group.getPlayers().contains(player)){
                group.removePlayer(player);
                groupRepository.save(group);
            }
        }

        playerRepository.delete(player);
    }
}
