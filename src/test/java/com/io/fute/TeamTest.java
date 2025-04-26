package com.io.fute;

import com.io.fute.entity.AppUser;
import com.io.fute.entity.Player;
import com.io.fute.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
public class TeamTest {

    @Mock
    AppUser user;
    private Team team;

    @BeforeEach
    void setUp(){
        team = new Team();
    }

    @Test
    @DisplayName("Average overall should be 50")
    void avgOverShouldBeFifty(){
        Player player1 = new Player("Player 1", (byte)20, user);
        Player player2 = new Player("Player 2", (byte)60, user);
        Player player3 = new Player("Player 3", (byte)40, user);
        Player player4 = new Player("Player 4", (byte)80, user);
        List<Player> players = List.of(player1, player2, player3, player4);

        team.addPlayers(players);

        assertThat(team.averageOverall()).isEqualTo(50);

    }

    @Test
    @DisplayName("Should throw exception when average calc is invoked with empty players list")
    void shouldThrowAvgWithEmptyPlayersList(){

        assertThatThrownBy(team::averageOverall).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw exception when trying to add the same player to the players list")
    void shouldThrowWhenAddRepeatedPlayer(){
        Player player = new Player();
        List<Player> players = List.of(player);

        team.addPlayer(player);

        assertThatThrownBy(()-> team.addPlayers(players)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()-> team.addPlayer(player)).isInstanceOf(IllegalArgumentException.class);
        assertThat(team.numberOfPlayers()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should throw exception when trying to add null to the players list")
    void shouldThrowWhenAddNullPlayer(){

        assertThatThrownBy(()-> team.addPlayer(null)).isInstanceOf(IllegalArgumentException.class);
        assertThat(team.numberOfPlayers()).isEqualTo(0);
    }

}
