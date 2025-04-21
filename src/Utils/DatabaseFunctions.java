package Utils;

import Models.AgesModel;
import Models.MatchModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import Models.PlayerModel;
import Models.StatsModel;
import Models.TeamModel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseFunctions {

    public static ArrayList<TeamModel> getAllTeams() {

        ArrayList<TeamModel> list = new ArrayList();

        try {

            Connection con = SqliteConnection.Connector();
            PreparedStatement pst = con.prepareStatement("Select * from Teams");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                list.add(new TeamModel(rs.getInt("id"),
                        rs.getString("name"), rs.getString("image"),
                        rs.getInt("played"),
                        rs.getInt("win"), rs.getInt("draw"), rs.getInt("lose"),
                        rs.getInt("gs"), rs.getInt("gc"), rs.getInt("gs") - rs.getInt("gc"),
                        rs.getInt("points"),rs.getString("capitan")));
            }

            return list;

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }

    }

    public static ArrayList<PlayerModel> getAllPlayers() {

        ArrayList<PlayerModel> list = new ArrayList();

        try {

            Connection con = SqliteConnection.Connector();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM Player");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                int gc = rs.getInt("goals_c");
                int cs = rs.getInt("cleansheets");

                if (!rs.getString("type").equals(PlayerType.GoalKeeper.toString())){
                    gc =-1;
                    cs = -1;
                }

                list.add(new PlayerModel(rs.getString("name"), rs.getString("type"), rs.getInt("team_id"),
                        rs.getInt("age"), rs.getInt("number"), rs.getInt("goals"),
                        gc,cs, rs.getInt("rank")));
            }

            return list;

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }

    }
    
    public static PlayerModel getPlayer(String name) {

        for (PlayerModel player : getAllPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;

    }

    public static ArrayList<PlayerModel> getTeamPlayers(int teamId) {

        ArrayList<PlayerModel> list = new ArrayList();

        for (PlayerModel player : getAllPlayers()) {

            if (player.getTeam() == teamId) {
                list.add(player);
            }
        }
        return list;

    }
    
    public static ArrayList<String> getTeamGoalkeepers(int teamId) {

        ArrayList<String> list = new ArrayList();

        for (PlayerModel player : getTeamPlayers(teamId)) {

            if (player.getType().equals(PlayerType.GoalKeeper.toString())) {
                list.add(player.getName());
            }
        }
        return list;

    }

    public static ArrayList<String> getAllTeamsNames() {

        ArrayList<String> list = new ArrayList();

        try {

            Connection con = SqliteConnection.Connector();
            PreparedStatement pst = con.prepareStatement("Select name from Teams");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                list.add(rs.getString(1));
            }

            return list;

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }

    }
    
    public static TeamModel getTeam(int id) {

        for (TeamModel team : getAllTeams()) {
            if (team.getId()==id) {
                return team;
            }
        }
        return null;

    }

    public static String getTeamImage(String teamName) {

        for (TeamModel team : getAllTeams()) {
            if (team.getName().equals(teamName)) {
                return team.getImage();
            }
        }
        return null;

    }

    public static ArrayList<MatchModel> getAllMatches() {
        ArrayList<MatchModel> list = new ArrayList();

        try {

            Connection con = SqliteConnection.Connector();
            PreparedStatement pst = con.prepareStatement("Select * from Matches");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Integer s1 = rs.getInt("ScoreTeam1");
                Integer s2 = rs.getInt("ScoreTeam2");

                if (s1 < 0 && s2 < 0) {
                    s1 = null;
                    s2 = null;
                }
                

                list.add(new MatchModel(rs.getInt("id"), rs.getInt("match_round"),
                        rs.getInt("home_team"), rs.getInt("away_team"), rs.getString("date"),
                        rs.getString("match_referee"), s1, s2,
                        rs.getString("stadium_name"),
                        rs.getString("GoalkeeperOfTeam1"), rs.getString("GoalkeeperOfTeam2"), rs.getString("time"),
                        new ArrayList<>(Arrays.asList(rs.getString("ScorersNames_Team1").split(","))),
                        new ArrayList<>(Arrays.asList(rs.getString("ScorersNames_Team2").split(","))),
                        rs.getInt("ishield")
                ));

            }

            return list;

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static ArrayList<MatchModel> getTeamMatches(int id) {

        ArrayList<MatchModel> matches = getAllMatches();
        ArrayList<MatchModel> list = new ArrayList();

        for(MatchModel match : matches){

            if(match.getHome_team()==id || match.getAway_team()==id){
                list.add(match);
            }

        }
        return list;
    }

    public static ArrayList<MatchModel> getRoundMatches(int round) {
        ArrayList<MatchModel> matches = getAllMatches();
        ArrayList<MatchModel> list = new ArrayList();

        for(MatchModel match : matches){

            if(match.getMatch_round()==round){
                list.add(match);
            }

        }
            return list;
    }

    public static ArrayList<MatchModel> getTeamHieldMatches(int id) {

        ArrayList<MatchModel> matches = getAllMatches();
        ArrayList<MatchModel> list = new ArrayList();

        for(MatchModel match : matches){

            if(match.getHome_team()==id || match.getAway_team()==id){
                if(match.getIshield()==1){
                    list.add(match);
                }

            }

        }
        return list;
    }

    public static ArrayList<MatchModel> getHieldMatches() {
        ArrayList<MatchModel> matches = getAllMatches();
        ArrayList<MatchModel> list = new ArrayList();

        for(MatchModel match : matches){

            if(match.getIshield()==1){
                list.add(match);
            }

        }
        return list;
    }

    public static double getAVG(ArrayList<PlayerModel> list) {

        int sum = 0;
        int c = 0;

        for (PlayerModel player : list) {
            sum += player.getAge();
            c++;
        }

        if (c == 0) {
            return 0;
        } else {
            return sum / c;
        }

    }

    public static ArrayList<AgesModel> getAgesAVG() {

        ArrayList<AgesModel> list = new ArrayList<>();
        ArrayList<TeamModel> teams = getAllTeams();

        for (TeamModel team : teams) {
            ArrayList<PlayerModel> players = getTeamPlayers(team.getId());
            list.add(new AgesModel(team.getName(), team.getImage(), getAVG(players)));
        }

        list.sort((t1, t2) -> Double.toString(t1.getAges_avg()).compareTo(Double.toString(t2.getAges_avg())));

        return list;

    }

    public static ArrayList<TeamModel> orederTeams(String orderby) {
        ArrayList<TeamModel> list = getAllTeams();
        if (orderby.equals(Consts.POINTS)) {
            list.sort((t1, t2) -> Integer.toString(t2.getPoints()).compareTo(Integer.toString(t1.getPoints())));
        } else if (orderby.equals(Consts.SCORES)) {
            list.sort((t1, t2) -> Integer.toString(t2.getDif()).compareTo(Integer.toString(t1.getDif())));
        }

        return list;
    }

    public static ArrayList<MatchModel> getDateMatches(String date) {

        ArrayList<MatchModel> matches = getAllMatches();
        ArrayList<MatchModel> list = new ArrayList();

        for (MatchModel match : matches) {
            if (match.getDate().equals(date)) {
                list.add(match);
            }
        }

        return list;

    }

    public static int getTeamID(String teamName) {

        ArrayList<TeamModel> teams = getAllTeams();

        for (TeamModel team : teams) {
            if (team.getName().equals(teamName)) {
                return team.getId();
            }
        }
        return 0;

    }

    public static String getTeamName(int id) {

        ArrayList<TeamModel> teams = getAllTeams();

        for (TeamModel team : teams) {
            if (team.getId() == id) {
                return team.getName();
            }
        }
        return "None";

    }
    
    public static ArrayList<StatsModel> getTopThreeScorers() {

        ArrayList<PlayerModel> players = getAllPlayers();
        
        List<PlayerModel> sortedlist = players.stream()
                .sorted(Comparator.comparingInt(PlayerModel::getGoals).reversed())
                .limit(3)
                .collect(Collectors.toList());
        
        ArrayList<StatsModel> result = new ArrayList<>();
        
        for(PlayerModel player : sortedlist){
            result.add(new StatsModel(player.getName(), player.getGoals(),player.getTeam()));
        }

        return result;
    }
    public static ArrayList<StatsModel> getTopThreeRanked() {

        ArrayList<PlayerModel> players = getAllPlayers();

        List<PlayerModel> sortedlist = players.stream()
                .sorted(Comparator.comparingInt(PlayerModel::getRank))
                .limit(3)
                .collect(Collectors.toList());
        
        ArrayList<StatsModel> result = new ArrayList<>();
        
        for(PlayerModel player : sortedlist){
            result.add(new StatsModel(player.getName(), player.getRank(),player.getTeam()));
        }

        return result;
    }
    public static ArrayList<StatsModel> getTopThreeCleans() {

        ArrayList<PlayerModel> players = getAllPlayers();

        List<PlayerModel> sortedlist = players.stream()
                .sorted(Comparator.comparingInt(PlayerModel::getCleansheets).reversed())
                .limit(3)
                .collect(Collectors.toList());
        
        ArrayList<StatsModel> result = new ArrayList<>();
        
        for(PlayerModel player : sortedlist){
            result.add(new StatsModel(player.getName(), player.getCleansheets(),player.getTeam()));
        }

        return result;
    }

}
