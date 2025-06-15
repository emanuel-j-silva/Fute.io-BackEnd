package com.io.fute.entity.player;

import jakarta.persistence.*;

@Entity
@Table
public class PlayerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long originalPlayerId;
    private String playerName;
    private String urlPhoto;
    private byte playerOverall;

    public PlayerHistory(Long originalPlayerId, String playerName, byte playerOverall, String urlPhoto) {
        this.originalPlayerId = originalPlayerId;
        this.playerName = playerName;
        this.urlPhoto = urlPhoto;
        this.playerOverall = playerOverall;
    }

    public PlayerHistory(Long originalPlayerId, String playerName, byte playerOverall) {
        this.originalPlayerId = originalPlayerId;
        this.playerName = playerName;
        this.playerOverall = playerOverall;
    }

    public Long getId() {
        return id;
    }

    public Long getOriginalPlayerId() {
        return originalPlayerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOriginalUrlPhoto() {
        return urlPhoto;
    }

    public byte getPlayerOverall() {
        return playerOverall;
    }
}
