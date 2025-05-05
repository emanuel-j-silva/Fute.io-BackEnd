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

    public Team(){}

    public Team(String numeralName) {
        this.numeralName = numeralName;
    }

    public String getNumeralName() {
        return numeralName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int numberOfPlayers(){
        return players.size();
    }

    public double averageOverall(){
        if (players.isEmpty()) throw new IllegalArgumentException("Não há jogadores nesse time");

        return players.stream().mapToInt(Player::getOverall)
                .average().orElseThrow(()-> new IllegalArgumentException("Erro ao calcular média"));
    }

    public void addPlayer(Player player){
        if (player == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
        if (players.contains(player)) throw new IllegalArgumentException("Esse jogador já pertence ao time.");
        this.players.add(player);
    }

    public void addPlayers(List<Player> players){
        if (players == null) throw new IllegalArgumentException("Lista não pode ser nula");
        for (Player player : players) {
            if (player == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
            if (this.players.contains(player)) {
                throw new IllegalArgumentException("Esse jogador já pertence ao time.");
            }
        }
        this.players.addAll(players);
    }

    public void removeAllPlayers(){
        players.clear();
    }

}
