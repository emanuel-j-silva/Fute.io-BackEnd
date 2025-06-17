package com.io.fute.entity.group;

import com.io.fute.entity.user.AppUser;
import com.io.fute.entity.player.Player;
import jakarta.persistence.*;

import java.util.*;

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
    private final Set<Player> players = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Group(){}

    public Group(String name, String location, AppUser user) {
        this.name = name;
        this.location = location;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AppUser getUser() {
        return user;
    }

    public String getLocation() {
        return location;
    }

    public Set<Player> getPlayers(){
        return players;
    }

    public int getNumberOfPlayers(){
        return players.size();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        if (!players.contains(player)){
            throw new IllegalArgumentException("Esse jogador não pertence ao grupo.");
        }
        players.remove(player);
    }

    public void removeAllPlayers(){
        if (!players.isEmpty()) players.clear();
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
