package com.io.fute.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

import static com.io.fute.entity.DrawConstants.*;

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

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "draw_id")
    private List<Team> teams;

    public Draw(){
        this.date = LocalDateTime.now();
        this.teams = new ArrayList<>();
    }

    public Draw(Group group){
        if (group == null) throw new EntityNotFoundException("Grupo não pode ser nulo");
        this.group = group;
        this.date = LocalDateTime.now();
        this.teams = new ArrayList<>();
    }

    public List<Team> fetchTeams(){
        if (teams.isEmpty()) throw new IllegalStateException("O sorteio ainda não foi realizado.");
        return teams;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getGroupName(){
        return this.group.getName();
    }

    public void perform(List<Player> players, int numberOfTeams) {
        performInputValidator(players, numberOfTeams);
        teams.clear();

        generateBalancedTeams(players, numberOfTeams);
    }

    private void generateBalancedTeams(List<Player> players, int numberOfTeams) {
        int i=0;
        List<Team> bestTeams = new ArrayList<>();
        List<Team> currentTeams;
        boolean firstAttempt = true;
        do{
            currentTeams = shuffleAndDistributePlayers(players, numberOfTeams);
            if (firstAttempt){
                bestTeams = currentTeams;
                firstAttempt = false;
            }

            if (calcMaxAverageDiff(currentTeams) < calcMaxAverageDiff(bestTeams)){
                bestTeams = currentTeams;
            }
            this.teams = bestTeams;

            i++;
        }while(!isBalanced(currentTeams) && i < MAX_ATTEMPTS);
    }

    private double calcMaxAverageDiff(List<Team> teams){
        DoubleSummaryStatistics stats = teams.stream().mapToDouble(Team::averageOverall).summaryStatistics();
        if (stats.getCount() == 0) throw new IllegalStateException("Não há times suficientes para calcular diferença");
        return (stats.getMax() - stats.getMin());
    }

    private boolean isBalanced(List<Team> teams) {
        return calcMaxAverageDiff(teams) <= MAX_DIFFERENCE;
    }

    private List<Team> shuffleAndDistributePlayers(List<Player> players, int numberOfTeams) {
        Collections.shuffle(players);

        List<Team> teamsToSort = new ArrayList<>();
        for(int i=0; i < numberOfTeams; i++){
            teamsToSort.add(new Team(String.valueOf(i+1)));
        }
        for(int i = 0; i < players.size(); i++) {
            int indexTeam = i % numberOfTeams;
            teamsToSort.get(indexTeam).addPlayer(players.get(i));
        }

        return teamsToSort;
    }

    private static void performInputValidator(List<Player> players, int numberOfTeams) {
        if (players == null) throw new IllegalArgumentException("Lista de jogadores não pode ser nula.");

        for (Player p: players){
            if (p == null) throw new IllegalArgumentException("Jogador não pode ser nulo");
        }

        if (numberOfTeams < 2 || players.size() <= numberOfTeams) {
            throw new IllegalArgumentException("Número de times inválido");
        }
    }
}
