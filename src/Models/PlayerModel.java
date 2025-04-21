package Models;

import Utils.Consts;
import Utils.PlayerType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerModel {
    
    private String name;
    private String type;
    private int team_id;
    private int age;
    private int number;
    private int goals;
    private int goals_c;
    private int cleansheets;
    private int rank;
    

    public PlayerModel(String name, String type, int team, int age, int number, int goals, int goals_c, int cleansheets, int rank) {
        this.name = name;
        this.type = type;
        this.team_id = team;
        this.age = age;
        this.number = number;
        this.goals = goals;
        this.goals_c = goals_c;
        this.cleansheets = cleansheets;
        this.rank = rank;
    }

    public PlayerModel(String name, String type, int team, int age, int number , int rank)  throws SQLException {
        this.name = name;
        this.type = type;
        this.team_id = team;
        this.age = age;
        this.number = number;
        this.rank = rank;
        
        String SQL = "insert into Player(name,type,age,team_id,number,rank)values(?,?,?,?,?,?)";
       
        Connection con = Utils.SqliteConnection.Connector();
        PreparedStatement pst =con.prepareStatement(SQL);
        
        pst.setString(1, name);
        pst.setString(2, type);
        pst.setInt(3, age);
        pst.setInt(4, team);
        pst.setInt(5, number);
        pst.setInt(6, rank);
        pst.executeUpdate();
        
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTeam() {
        return team_id;
    }

    public void setTeam(int team) {
        this.team_id = team;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getGoals_c() {
        return goals_c;
    }

    public void setGoals_c(int goals_c) {
        this.goals_c = goals_c;
    }

    public int getCleansheets() {
        return cleansheets;
    }

    public void setCleansheets(int cleansheets) {
        this.cleansheets = cleansheets;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
    
    public String getDes() {
        if(getType().equals(PlayerType.GoalKeeper.toString())){
            return Consts.DES_G;
        }
        else if(getType().equals(PlayerType.Defender.toString())){
            return Consts.DES_D;
        }
        else if(getType().equals(PlayerType.Midfielder.toString())){
            return Consts.DES_M;
        }
        else{
            return Consts.DES_A;
        }
    }
    
    
    
    
    
}
