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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_id")
    private List<PlayerHistory> players = new ArrayList<>();

    public Team(){}

    public Team(String numeralName) {
        this.numeralName = numeralName;
    }

    public String getNumeralName() {
        return numeralName;
    }

    public List<PlayerHistory> getPlayers() {
        return players;
    }

    public int numberOfPlayers(){
        return players.size();
    }

    public double averageOverall(){
        if (players.isEmpty()) throw new IllegalArgumentException("Não há jogadores nesse time");

        return players.stream().mapToInt(PlayerHistory::getPlayerOverall)
                .average().orElseThrow(()-> new IllegalArgumentException("Erro ao calcular média"));
    }

    public void addPlayer(Player player){
        if (player == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
        PlayerHistory playerHistory = new PlayerHistory(player.getId(), player.getName(), player.getOverall());
        this.players.add(playerHistory);
    }

    public void addPlayers(List<Player> players){
        if (players == null) throw new IllegalArgumentException("Lista não pode ser nula");
        for (Player player : players) {
            if (player == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
            if (this.players.contains(player)) {
                throw new IllegalArgumentException("Esse jogador já pertence ao time.");
            }
        }

        for(Player player: players){
            PlayerHistory playerHistory = new PlayerHistory(player.getId(), player.getName(), player.getOverall());
            this.players.add(playerHistory);
        }
    }

    public void removeAllPlayers(){
        players.clear();
    }

}
