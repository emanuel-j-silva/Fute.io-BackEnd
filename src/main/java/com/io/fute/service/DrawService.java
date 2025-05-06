package com.io.fute.service;

import com.io.fute.dto.draw.DrawInfo;
import com.io.fute.dto.draw.DrawRequest;
import com.io.fute.dto.player.PlayerInfo;
import com.io.fute.dto.team.TeamInfo;
import com.io.fute.entity.*;
import com.io.fute.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DrawService {
    private final DrawRepository drawRepository;
    private final AppUserRepository userRepository;
    private final GroupRepository groupRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public DrawService(DrawRepository drawRepository, AppUserRepository userRepository, GroupRepository groupRepository, TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.drawRepository = drawRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public DrawInfo performDraw(UUID userId, UUID groupId, DrawRequest request){
        if (userRepository.findById(userId).isEmpty()) throw new EntityNotFoundException("Usuário não encontrado.");

        Group group = validateAndFetchGroup(userId, groupId);

        List<Player> players = validateAndFetchPlayers(userId, request.playerIds(), group);

        Draw draw = new Draw(group);
        draw.perform(players, request.numberOfTeams());
        drawRepository.save(draw);

        return mapToDrawInfo(draw);
    }

    private static DrawInfo mapToDrawInfo(Draw draw) {
        List<TeamInfo> teamInfoList = new ArrayList<>();

        for (Team team: draw.fetchTeams()){
            List<PlayerInfo> playerInfoList = team.getPlayers().stream()
                    .map(p -> new PlayerInfo(p.getId(), p.getName(), p.getOverall(), p.getUrlPhoto())).toList();

            teamInfoList.add(new TeamInfo(team.getNumeralName(),playerInfoList));
        }

        return new DrawInfo(draw.getDate(), teamInfoList);
    }

    private List<Player> validateAndFetchPlayers(UUID userId, List<Long> playerIds, Group group) {
        List<Player> players = new ArrayList<>();
        for (Long playerId: playerIds){
            Player player = playerRepository.findById(playerId)
                    .orElseThrow(()-> new EntityNotFoundException("Jogador não encontrado"));

            if (!player.getUser().getId().equals(userId)){
                throw new IllegalArgumentException("Este jogador não pode ser adicionado");
            }
            if (!group.getPlayers().contains(player)) {
                throw new IllegalArgumentException("Este jogador não pertence ao grupo");
            }

            players.add(player);
        }
        return players;
    }

    private Group validateAndFetchGroup(UUID userId, UUID groupId) {
        Group group  = groupRepository.findById(groupId)
                .orElseThrow(()-> new EntityNotFoundException("Grupo não encontrado."));

        if (!group.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Você não tem permissão para modificar esse grupo");
        }
        return group;
    }
}
