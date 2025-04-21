package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.image.Image;

public class TeamModel {

    private int id;
    private String name;
    private String image;
    private int played;
    private int win;
    private int draw;
    private int lose;
    private int gs;
    private int gc;
    private int dif;
    private int points;
    private String capitan;

    public TeamModel(String name, String image) throws SQLException {
        this.name = name;
        this.image = image;
        
        String SQL = "insert into TEAMS(name,image)values(?,?)";
       
        Connection con = Utils.SqliteConnection.Connector();
        PreparedStatement pst =con.prepareStatement(SQL);
        
        pst.setString(1, name);
        pst.setString(2, image);
        pst.executeUpdate();
        
    }

    public TeamModel(int id, String name, String image, int played, int win, int draw, int lose, int gs, int gc, int dif, int points , String capitan) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.gs = gs;
        this.gc = gc;
        this.dif = dif;
        this.points = points;
        this.capitan = capitan;
    }


    public String getCapitan() {
        return capitan;
    }

    public void setCapitan(String capitan) {
        this.capitan = capitan;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getGs() {
        return gs;
    }

    public void setGs(int gs) {
        this.gs = gs;
    }

    public int getGc() {
        return gc;
    }

    public void setGc(int gc) {
        this.gc = gc;
    }

    public int getDif() {
        return dif;
    }

    public void setDif(int dif) {
        this.dif = dif;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
