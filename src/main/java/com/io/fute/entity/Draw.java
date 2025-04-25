package com.io.fute.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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
    }

    public Set<Team> fetchTeams(){
        return teams;
    }

}
