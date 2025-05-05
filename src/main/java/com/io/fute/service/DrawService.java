package com.io.fute.service;

import com.io.fute.dto.draw.DrawInfo;
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
    public DrawInfo performDraw(UUID userId, UUID groupId, List<Player> players, int numberOfTeams){
        if (userRepository.findById(userId).isEmpty()) throw new EntityNotFoundException("Usuário não encontrado.");

        Group group  = groupRepository.findById(groupId)
                .orElseThrow(()-> new EntityNotFoundException("Grupo não encontrado."));

        if (!group.getUser().getId().equals(userId)){
            throw new IllegalArgumentException("Você não tem permissão para modificar esse grupo");
        }

        for (Player player:players){
            if (playerRepository.findById(player.getId()).isEmpty()){
                throw new EntityNotFoundException("Jogador não encontrado");
            }
            if (!player.getUser().getId().equals(userId)){
                throw new IllegalArgumentException("Este jogador não pode ser adicionado");
            }
            if (!group.getPlayers().contains(player)) {
                throw new IllegalArgumentException("Este jogador não pertence ao grupo");
            }

        }

        Draw draw = new Draw(group);
        draw.perform(players, numberOfTeams);
        drawRepository.save(draw);

        List<TeamInfo> teamInfoList = new ArrayList<>();

        for (Team t:draw.fetchTeams()){
            List<PlayerInfo> playerInfoList = t.getPlayers().stream()
                    .map(p -> new PlayerInfo(p.getName(), p.getOverall(), p.getUrlPhoto())).toList();

            teamInfoList.add(new TeamInfo(t.getNumeralName(),playerInfoList));
        }

        return new DrawInfo(draw.getDate(), teamInfoList);
    }
}
