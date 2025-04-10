package com.io.fute.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Objects;

@Entity
@Table
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Min(value = 1)
    @Max(value = 100)
    private byte overall;
    private String urlPhoto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Player(){}

    public Player(String name, byte overall, AppUser user) {
        this.name = name;
        this.overall = overall;
        this.user = user;
    }

    public Player(String name, byte overall, String urlPhoto, AppUser user) {
        this.name = name;
        this.overall = overall;
        this.urlPhoto = urlPhoto;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public byte getOverall() {
        return overall;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changeOverall(byte overall){
        this.overall = overall;
    }

    public void changePhoto(String urlPhoto){
        this.urlPhoto = urlPhoto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(id, player.id) && Objects.equals(user, player.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
