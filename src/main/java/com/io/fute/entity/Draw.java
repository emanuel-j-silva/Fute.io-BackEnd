package com.io.fute.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "draw", orphanRemoval = true)
    private List<Team> teams;

    public Draw(){
        this.date = LocalDateTime.now();
        this.teams = new ArrayList<>();
    }

    public Draw(Group group){
        if (group == null) throw new EntityNotFoundException("Grupo não pode ser nulo");
        this.group = group;
        this.date = LocalDateTime.now();
        this.teams = new ArrayList<>();
    }

    public List<Team> fetchTeams(){
        if (teams.isEmpty()) throw new IllegalStateException("O sorteio ainda não foi realizado.");
        return teams;
    }

    public void perform(List<Player> players, int numberOfTeams) {
        performInputValidator(players, numberOfTeams);
        teams.clear();

        for(int i=0; i < numberOfTeams; i++){
            teams.add(new Team(String.valueOf(i+1)));
        }

        boolean hasAcceptableDifference = false;
        while(!hasAcceptableDifference){
            for(Team t: teams){
                t.removeAllPlayers();
            }
            Collections.shuffle(players);

            for(int i=0; i < players.size(); i++){
                int indexTeam = i % numberOfTeams;
                teams.get(indexTeam).addPlayer(players.get(i));
            }
            OptionalDouble max = teams.stream().mapToDouble(Team::averageOverall).max();
            OptionalDouble min = teams.stream().mapToDouble(Team::averageOverall).min();
            if (max.isPresent() && min.isPresent()){
                hasAcceptableDifference = (max.getAsDouble() - min.getAsDouble()) <= 15;
            }
        }

    }

    private static void performInputValidator(List<Player> players, int numberOfTeams) {
        if (players == null) throw new IllegalArgumentException("Lista de jogadores não pode ser nula.");

        for (Player p: players){
            if (p == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
        }

        if (numberOfTeams < 2 || players.size() <= numberOfTeams) {
            throw new IllegalArgumentException("Número de times inválido");
        }
    }
}
