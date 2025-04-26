package com.io.fute;

import com.io.fute.entity.Draw;
import com.io.fute.entity.Player;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
public class DrawTest {

    private Draw draw;

    @BeforeEach
    void setUp(){
        draw = new Draw();
    }

    @Test
    @DisplayName("Should throw exception when group in constructor is null")
    void shouldThrowWhenGroupIsNull(){
        assertThatThrownBy(()->new Draw(null)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw exception when fetch teams is called with teams set empty")
    void shouldThrowWhenTeamsSetIsEmpty(){
        assertThatThrownBy(draw::fetchTeams).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should throw exception when players list is null")
    void shouldThrowPlayersListNull(){
        assertThatThrownBy(()-> draw.perform(null,2))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("Should throw exception when any player in the players list is null")
    void shouldThrowPlayerIsNull(){
        List<Player> players = new ArrayList<>();
        players.add(null);
        players.add(new Player());
        assertThatThrownBy(()-> draw.perform(players,2))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
