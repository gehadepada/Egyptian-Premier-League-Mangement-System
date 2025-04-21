package Models;

import Utils.SqliteConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author gehad
 */
public class MatchModel {

    private int id;
    private int match_round;
    private int home_team;
    private int away_team;
    private String date;
    private String match_referee;
    private Integer ScoreTeam1;
    private Integer ScoreTeam2;
    private String stadium_name;
    
    private String GoalkeeperOfTeam1;
    private String GoalkeeperOfTeam2;
    private ArrayList<String> ScorersNames_Team1 ;
    private ArrayList<String> ScorersNames_Team2 ;
    private String time;
    
    private int ishield;
    

    public MatchModel(int id, int match_round, int team1, int team2, String date, String match_referee, Integer ScoreTeam1, Integer ScoreTeam2, String stadium_name, 
            String GoalkeeperOfTeam1, String GoalkeeperOfTeam2,String time ,ArrayList<String> list1,ArrayList<String> list2,int ishield) {
        this.id = id;
        this.match_round = match_round;
        this.home_team = team1;
        this.away_team = team2;
        this.date = date;
        this.match_referee = match_referee;
        this.ScoreTeam1 = ScoreTeam1;
        this.ScoreTeam2 = ScoreTeam2;
        this.stadium_name = stadium_name;
        this.GoalkeeperOfTeam1 = GoalkeeperOfTeam1;
        this.GoalkeeperOfTeam2 = GoalkeeperOfTeam2;
        this.time = time;
        this.ishield =ishield;
        this.ScorersNames_Team1 =list1;
        this.ScorersNames_Team2 = list2;
    }

    public MatchModel(int match_round, int home_team, int away_team, String date, String match_referee, String stadium_name ,String time) throws SQLException {
        this.match_round = match_round;
        this.home_team = home_team;
        this.away_team = away_team;
        this.date = date;
        this.match_referee = match_referee;
        this.stadium_name = stadium_name;
        this.time = time;
        
        String SQL = "insert into Matches(match_round,home_team,away_team,date,match_referee,stadium_name,time,ScorersNames_Team1,ScorersNames_Team2)values(?,?,?,?,?,?,?,?,?)";
        Connection con = Utils.SqliteConnection.Connector();
        PreparedStatement pst = con.prepareStatement(SQL);

        pst.setInt(1, match_round);
        pst.setInt(2, home_team);
        pst.setInt(3, away_team);
        pst.setString(4, date);
        pst.setString(5, match_referee);
        pst.setString(6, stadium_name);
        pst.setString(7, time);
        pst.setString(8, "");
        pst.setString(9, "");
        pst.executeUpdate();

        
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatch_round() {
        return match_round;
    }

    public void setMatch_round(int match_round) {
        this.match_round = match_round;
    }

    public int getHome_team() {
        return home_team;
    }

    public void setHome_team(int home_team) {
        this.home_team = home_team;
    }

    public int getAway_team() {
        return away_team;
    }

    public void setAway_team(int away_team) {
        this.away_team = away_team;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatch_referee() {
        return match_referee;
    }

    public void setMatch_referee(String match_referee) {
        this.match_referee = match_referee;
    }

    public Integer getScoreTeam1() {
        return ScoreTeam1;
    }

    public void setScoreTeam1(Integer ScoreTeam1) {
        this.ScoreTeam1 = ScoreTeam1;
    }

    public Integer getScoreTeam2() {
        return ScoreTeam2;
    }

    public void setScoreTeam2(Integer ScoreTeam2) {
        this.ScoreTeam2 = ScoreTeam2;
    }

    public String getStadium_name() {
        return stadium_name;
    }

    public void setStadium_name(String stadium_name) {
        this.stadium_name = stadium_name;
    }

    public ArrayList<String> getScorersNames_Team1() {
        return ScorersNames_Team1;
    }

    public void setScorersNames_Team1(ArrayList<String> ScorersNames_Team1) {
        this.ScorersNames_Team1 = ScorersNames_Team1;
    }

    public ArrayList<String> getScorersNames_Team2() {
        return ScorersNames_Team2;
    }

    public void setScorersNames_Team2(ArrayList<String> ScorersNames_Team2) {
        this.ScorersNames_Team2 = ScorersNames_Team2;
    }

    public String getGoalkeeperOfTeam1() {
        return GoalkeeperOfTeam1;
    }

    public void setGoalkeeperOfTeam1(String GoalkeeperOfTeam1) {
        this.GoalkeeperOfTeam1 = GoalkeeperOfTeam1;
    }

    public String getGoalkeeperOfTeam2() {
        return GoalkeeperOfTeam2;
    }

    public void setGoalkeeperOfTeam2(String GoalkeeperOfTeam2) {
        this.GoalkeeperOfTeam2 = GoalkeeperOfTeam2;
    }

    public int getIshield() {
        return ishield;
    }

    public void setIshield(int ishield) {
        this.ishield = ishield;
    }

    public boolean IsPlayed() {
        if (getScoreTeam1() != null && getScoreTeam2() != null) {
            return true;
        } else {
            return false;
        }
    }

}
