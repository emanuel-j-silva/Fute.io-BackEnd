package com.io.fute.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeralName;

    @ManyToMany
    @JoinTable(name = "team_player",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players = new ArrayList<>();

    public int numberOfPlayers(){
        return players.size();
    }

    public double averageOverall(){
        if (players.isEmpty()) throw new IllegalArgumentException("Não há jogadores nesse time");

        return players.stream().mapToInt(Player::getOverall)
                .average().orElseThrow(()-> new IllegalArgumentException("Erro ao calcular média"));
    }

}
