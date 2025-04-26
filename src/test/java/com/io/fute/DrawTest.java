package com.io.fute;

import com.io.fute.entity.Draw;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}
