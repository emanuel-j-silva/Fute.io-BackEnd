package com.io.fute.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "app_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String location;

    @ManyToMany
    @JoinTable(
            name = "group_player",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private final List<Player> players = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Group(String name, String location, AppUser user) {
        this.name = name;
        this.location = location;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int getNumberOfPlayers(){
        return players.size();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group group)) return false;
        return Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", number of players='" + getNumberOfPlayers() + '\'' +
                '}';
    }
}
