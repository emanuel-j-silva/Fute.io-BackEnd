package com.io.fute.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Team> teams;

    public Draw(){
        this.date = LocalDateTime.now();
        this.teams = new HashSet<>();
    }

    public Draw(Group group){
        if (group == null) throw new EntityNotFoundException("Grupo não pode ser nulo");
        this.group = group;
        this.date = LocalDateTime.now();
        this.teams = new HashSet<>();
    }

    public Set<Team> fetchTeams(){
        if (teams.isEmpty()) throw new IllegalStateException("O sorteio ainda não foi realizado.");
        return teams;
    }

    public void perform(List<Player> players, int numberOfTeams) {
        if (players == null) throw new IllegalArgumentException("Lista de jogadores não pode ser nula.");

        for (Player p:players){
            if (p == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
        }

        if (numberOfTeams < 2) throw new IllegalArgumentException("Número de times inválido");
    }
}
