package com.io.fute.service;

import com.io.fute.dto.group.GroupInfo;
import com.io.fute.dto.group.GroupRequest;
import com.io.fute.dto.player.AssociatePlayersRequest;
import com.io.fute.dto.player.PlayerInfo;
import com.io.fute.entity.user.AppUser;
import com.io.fute.entity.group.Group;
import com.io.fute.entity.player.Player;
import com.io.fute.repository.AppUserRepository;
import com.io.fute.repository.GroupRepository;
import com.io.fute.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final AppUserRepository userRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, AppUserRepository userRepository, PlayerRepository playerRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    public void createGroup(GroupRequest groupRequest, UUID userId){
        AppUser user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("Usuário não encontrado"));
        if (groupRepository.existsByNameAndUser(groupRequest.name(), user)){
            throw new IllegalArgumentException("Você já possui um grupo com esse nome");
        }

        Group savedGroup = new Group(groupRequest.name(),groupRequest.location(),user);
        groupRepository.save(savedGroup);
    }

    public List<GroupInfo> fetchAllGroupsByUser(UUID userId){
        return groupRepository.findAllByUserId(userId).stream()
                .map(group -> new GroupInfo(group.getId(), group.getName(),group.getLocation(), group.getNumberOfPlayers()))
                .toList();
    }

    public List<PlayerInfo> fetchAllPlayersByGroup(UUID groupId, UUID userId){
        Group group = fetchAndValidateGroup(groupId, userId);

        return group.getPlayers()
                .stream()
                .sorted(Comparator.comparingInt(Player::getOverall).reversed())
                .map(player -> new PlayerInfo(player.getId(), player.getName(),
                        player.getOverall(), player.getUrlPhoto()))
                .toList();
    }

    public List<PlayerInfo> fetchPlayersNotInGroup(UUID groupId, UUID userId){
        Group group = fetchAndValidateGroup(groupId, userId);

        var players = playerRepository.findAllByUserIdOrderByOverallDesc(userId);

        return players.stream().filter(player ->
                !group.getPlayers().contains(player))
                .map(player -> new PlayerInfo(player.getId(), player.getName(),
                        player.getOverall(), player.getUrlPhoto()))
                .toList();

    }

    public void addPlayersToGroup(UUID groupId, AssociatePlayersRequest requestPlayers, UUID userId){
        Group group = fetchAndValidateGroup(groupId, userId);
        for(Long playerId:requestPlayers.playerIds()){
            Player player = fetchAndValidatePlayer(playerId, userId);

            if (group.getPlayers().contains(player)){
                throw new IllegalArgumentException("Esse jogador já pertence a este grupo");
            }
            group.addPlayer(player);
        }

        groupRepository.save(group);
    }

    public void removePlayerFromGroup(UUID groupId, Long playerId, UUID userId){
        Group group = fetchAndValidateGroup(groupId, userId);
        Player player = fetchAndValidatePlayer(playerId, userId);

        group.removePlayer(player);
        groupRepository.save(group);
    }

    private Group fetchAndValidateGroup(UUID groupId, UUID userId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(()-> new EntityNotFoundException("Grupo não encontrado."));

        if (!group.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Você não tem permissão para modificar esse grupo");
        }

        return group;
    }
    private Player fetchAndValidatePlayer(Long playerId, UUID userId){
        Player player = playerRepository.findById(playerId)
                .orElseThrow(()-> new EntityNotFoundException("Jogador não encontrado."));

        if (!player.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Você não tem permissão para acessar esse jogador");
        }

        return player;
    }
}
