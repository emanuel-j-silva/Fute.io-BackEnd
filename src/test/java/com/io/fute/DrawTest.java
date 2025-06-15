package com.io.fute;

import com.io.fute.entity.draw.Draw;
import com.io.fute.entity.group.Group;
import com.io.fute.entity.player.Player;
import com.io.fute.entity.team.Team;
import com.io.fute.entity.user.AppUser;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;


import static org.assertj.core.api.Assertions.*;
import static com.io.fute.entity.draw.DrawConstants.*;
public class DrawTest {

    private final AppUser user = new AppUser();

    private Group group = new Group();

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

    @Test
    @DisplayName("Should throw exception when number of teams is negative or zero")
    void shouldThrowNegativeNumTeams(){
        var player = new Player();
        List<Player> players = new ArrayList<>();

        players.add(player);

        assertThatThrownBy(()-> draw.perform(players, -1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(()-> draw.perform(players, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw exception when number of teams is less than two")
    void shouldThrowLessTwoNumTeams(){
        var player = new Player();
        List<Player> players = new ArrayList<>();

        players.add(player);
        assertThatThrownBy(()-> draw.perform(players, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw exception when number of players is less or equal than teams number")
    void shouldThrowNumPlayersLessOrEqualTeams(){
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        List<Player> players = List.of(player1, player2, player3);

        assertThatThrownBy(()-> draw.perform(players, 4))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(()-> draw.perform(players, 3))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should form teams correctly with the indicated value")
    void shouldFormTeamsCorrectQuantity(){
        int numTeams = 2;
        Player player1 = new Player("Player 1", (byte) 50, user);
        Player player2 = new Player("Player 2", (byte) 50, user);
        Player player3 = new Player("Player 3", (byte) 50, user);
        Player player4 = new Player("Player 4", (byte) 50, user);
        List<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));

        draw.perform(players, numTeams);
        assertThat(draw.fetchTeams().size()).isEqualTo(numTeams);
    }

    @Test
    @DisplayName("Should form teams with equal number of players when the quantity allows")
    void shouldFormTeamsEqualNumOfPlayers(){
        Player player1 = new Player("Player 1", (byte) 50, user);
        Player player2 = new Player("Player 2", (byte) 50, user);
        Player player3 = new Player("Player 3", (byte) 50, user);
        Player player4 = new Player("Player 4", (byte) 50, user);
        List<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));

        draw.perform(players, 2);

        boolean hasUniqueValue = draw.fetchTeams().stream().map(Team::numberOfPlayers).distinct().count() <= 1;
        assertThat(hasUniqueValue).isTrue();
    }

    @Test
    @DisplayName("Should form teams with a difference of at most 1 between the number of players")
    void shouldFormTeamsBalancedNumOfPlayers(){
        Player player1 = new Player("Player 1", (byte) 50, user);
        Player player2 = new Player("Player 2", (byte) 50, user);
        Player player3 = new Player("Player 3", (byte) 50, user);
        Player player4 = new Player("Player 4", (byte) 50, user);
        Player player5 = new Player("Player 5", (byte) 50, user);
        List<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4, player5));

        draw.perform(players, 2);

        List<Team> teams = draw.fetchTeams();
        OptionalInt max = teams.stream().mapToInt(Team::numberOfPlayers).max();
        OptionalInt min = teams.stream().mapToInt(Team::numberOfPlayers).min();

        boolean hasAcceptableDifference = false;
        if (max.isPresent() && min.isPresent()){
            hasAcceptableDifference = (max.getAsInt() - min.getAsInt()) <= 1;
        }

        assertThat(hasAcceptableDifference).isTrue();
    }

    @Test
    @DisplayName("Should form teams with a numeral name correctly")
    void shouldAddTeamNumeralNameCorrectly(){
        Player player1 = new Player("Player 1", (byte) 50, user);
        Player player2 = new Player("Player 2", (byte) 50, user);
        Player player3 = new Player("Player 3", (byte) 50, user);
        Player player4 = new Player("Player 4", (byte) 50, user);
        List<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));

        draw.perform(players, 2);
        List<Team> teams = draw.fetchTeams();

        boolean hasCorrectNumeralName = true;
        int i=0;
        while(hasCorrectNumeralName && i < teams.size()){
            hasCorrectNumeralName = (teams.get(i).getNumeralName().equals(String.valueOf(i+1)));
            i++;
        }
        assertThat(hasCorrectNumeralName).isTrue();
    }

    @Test
    @DisplayName("Should form balanced teams with only three players")
    void shouldFormBalancedTeamsThreePlayers(){
        Player player1 = new Player("Player 1", (byte) 50, user);
        Player player2 = new Player("Player 2", (byte) 10, user);
        Player player3 = new Player("Player 3", (byte) 100, user);

        List<Player> players = new ArrayList<>(List.of(player1, player2, player3));
        draw.perform(players, 2);

        List<Team> teams = draw.fetchTeams();
        OptionalDouble max = teams.stream().mapToDouble(Team::averageOverall).max();
        OptionalDouble min = teams.stream().mapToDouble(Team::averageOverall).min();

        boolean hasAcceptableDifference = false;
        if (max.isPresent() && min.isPresent()){
            hasAcceptableDifference = (max.getAsDouble() - min.getAsDouble()) <= MAX_DIFFERENCE;
        }

        assertThat(hasAcceptableDifference).isTrue();
    }

    @Test
    @DisplayName("Should form the best possible draw when it is not possible to reach the threshold")
    void shouldFormBestPossibleOverThreshold(){
        Player player1 = new Player("Player 1", (byte) 10, user);
        Player player2 = new Player("Player 2", (byte) 10, user);
        Player player3 = new Player("Player 3", (byte) 100, user);
        List<Player> players = new ArrayList<>(List.of(player1, player2, player3));

        draw.perform(players, 2);
        List<Team> teams = draw.fetchTeams();
        OptionalDouble max = teams.stream().mapToDouble(Team::averageOverall).max();
        OptionalDouble min = teams.stream().mapToDouble(Team::averageOverall).min();

        double maxAvgDifference = 0;
        if (max.isPresent() && min.isPresent()){
            maxAvgDifference = (max.getAsDouble() - min.getAsDouble());
        }

        assertThat(maxAvgDifference).isEqualTo(45);
    }
}
