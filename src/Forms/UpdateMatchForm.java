package Forms;

import Models.MatchModel;
import Models.PlayerModel;
import Models.TeamModel;
import Utils.Consts;
import Utils.DatabaseFunctions;
import Utils.PlayerType;
import Utils.SharedModel;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class UpdateMatchForm {

    private Stage window;
    Scene scene ;
    private ArrayList<ComboBox<String>> team1ScorerFields = new ArrayList<>();
    private ArrayList<ComboBox<String>> team2ScorerFields = new ArrayList<>();

    public void display(MatchModel selectedmatch) {

        ArrayList<String> teams = DatabaseFunctions.getAllTeamsNames();
        teams.add(Consts.NO_TEAM);

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setOnCloseRequest(event -> afterClose());


        Label home_label = new Label("Home Team");
        Label away_label = new Label("Away Team");

        Rectangle background1 = new Rectangle(128, 128, Color.TRANSPARENT);
        Rectangle background2 = new Rectangle(128, 128, Color.TRANSPARENT);

        ImageView home_logo = new ImageView();
        ImageView away_logo = new ImageView();

        home_logo.setFitHeight(128);
        home_logo.setFitWidth(128);
        home_logo.setPreserveRatio(true);

        away_logo.setFitHeight(128);
        away_logo.setFitWidth(128);
        away_logo.setPreserveRatio(true);

        Label team1_label = new Label(DatabaseFunctions.getTeamName(selectedmatch.getHome_team()));
        Label team2_label = new Label(DatabaseFunctions.getTeamName(selectedmatch.getAway_team()));

        TextField score1_Field = new TextField("score");
        TextField score2_Field = new TextField("score");

        ComboBox gk1_entry = new ComboBox<>();
        ComboBox gk2_entry = new ComboBox<>();

        gk1_entry.getItems().setAll(DatabaseFunctions.getTeamGoalkeepers(selectedmatch.getHome_team()));
        gk2_entry.getItems().setAll(DatabaseFunctions.getTeamGoalkeepers(selectedmatch.getAway_team()));

        gk1_entry.setValue("Startup Goalkeeper");
        gk2_entry.setValue("Startup Goalkeeper");

        Label ref_label = new Label("Referee Name");
        Label stad_label = new Label("Stadium Name");

        TextField ref_Field = new TextField(selectedmatch.getMatch_referee());
        TextField stad_Field = new TextField(selectedmatch.getStadium_name());

        Label date_label = new Label("Match Date");
        DatePicker date_picker = new DatePicker();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate date = LocalDate.parse(selectedmatch.getDate(), formatter);
        date_picker.setValue(date);
        date_picker.setEditable(false);

        Label time_label = new Label("Match Time");
        TextField time_Field = new TextField(selectedmatch.getTime());

        Button ConfirmButton = new Button("Next");

        StackPane homeContainer = new StackPane();
        StackPane awayContainer = new StackPane();

        homeContainer.getChildren().addAll(background1, home_logo);
        awayContainer.getChildren().addAll(background2, away_logo);

        VBox homeLayout = new VBox(10);
        VBox awayLayout = new VBox(10);
        HBox topLayout = new HBox();

        HBox layout1 = new HBox();
        HBox layout2 = new HBox();
        HBox layout3 = new HBox();
        HBox layout4 = new HBox();
        HBox layout5 = new HBox();
        VBox bottomLayout = new VBox(10);

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(15));

        homeLayout.getChildren().addAll(home_label, homeContainer, team1_label, score1_Field, gk1_entry);
        awayLayout.getChildren().addAll(away_label, awayContainer, team2_label, score2_Field, gk2_entry);
        topLayout.getChildren().addAll(homeLayout, awayLayout);

        layout1.getChildren().addAll(ref_label, ref_Field);
        layout2.getChildren().addAll(stad_label, stad_Field);
        layout3.getChildren().addAll(ConfirmButton);
        layout4.getChildren().addAll(date_label, date_picker);
        layout5.getChildren().addAll(time_label, time_Field);

        bottomLayout.getChildren().addAll(layout4, layout5, layout1, layout2, layout3);

        try {

            File file1 = new File(DatabaseFunctions.getTeamImage(team1_label.getText()));
            File file2 = new File(DatabaseFunctions.getTeamImage(team2_label.getText()));

            Image image1 = new Image(new FileInputStream(file1));
            Image image2 = new Image(new FileInputStream(file2));

            home_logo.setImage(image1);
            away_logo.setImage(image2);

        } catch (Exception e) {
        }

        homeLayout.setId("vboxlayout");
        awayLayout.setId("vboxlayout");

        topLayout.setId("layout");
        bottomLayout.setId("layout");

        layout1.setId("hboxlayout");
        layout2.setId("hboxlayout");
        layout4.setId("hboxlayout");
        layout5.setId("hboxlayout");

        layout3.setId("hboxlayout2");

        formLayout.getChildren().addAll(topLayout, bottomLayout);

        ConfirmButton.setOnAction(event -> {

            try {
                int id = selectedmatch.getId();
                int s1 = Integer.parseInt(score1_Field.getText().toString());
                int s2 = Integer.parseInt(score2_Field.getText().toString());
                String ndate = date_picker.getValue().toString();
                String ntime = time_Field.getText();
                String nstd = stad_Field.getText();
                String nref = ref_Field.getText();
                String ngk1 = gk1_entry.getValue().toString();
                String ngk2 = gk2_entry.getValue().toString();
                ArrayList<String> list1 = new ArrayList();
                ArrayList<String> list2 = new ArrayList();

                
                check(id, ndate, ntime, s1, s2, nstd, nref, ngk1, ngk2,list1 ,list2);

            } catch (NumberFormatException e) {

                showAlert(Alert.AlertType.ERROR, "ERROR", e.getLocalizedMessage());
            }

        });

        scene = new Scene(formLayout);
        scene.getStylesheets().add("Drawable/style.css");
        window.setTitle("Update Match Details");
        window.setScene(scene);
        window.show();
    }

    private void check(int id, String date, String time, int s1, int s2, String stdm, String ref,
            String gk1, String gk2, ArrayList<String> scores1, ArrayList<String> scores2) {

        if (s1 < 0 || s2 < 0) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Please Enter Valid Values1");
        } else if (date.isEmpty() || time.isEmpty() || stdm.isEmpty() || ref.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Please Fill all Fields");
        }
        else {
            if(s1==0&&s2==0){
                updateMatchInDatabase(id, date, time, s1, s2, stdm, ref, gk1, gk2, scores1, scores2);
            }
            else{
                displayGoalScorerForm(id, date, time, s1, s2, stdm, ref, gk1, gk2);
            }
        }

    }

    public void displayGoalScorerForm(int id, String date, String time, int s1, int s2, String stdm, String ref, String gk1, String gk2){
        scene = null;
        Stage nextStage = new Stage();
        VBox pane = new VBox();

        ArrayList<String> team1Players = new ArrayList<>();
        ArrayList<String> team2Players = new ArrayList<>();

        team1Players.add(Consts.OWN_GOAL);
        team2Players.add(Consts.OWN_GOAL);


        for(PlayerModel player : DatabaseFunctions.getTeamPlayers(SharedModel.getSelected_match().getHome_team())){
            team1Players.add(player.getName());
        }
        for(PlayerModel player : DatabaseFunctions.getTeamPlayers(SharedModel.getSelected_match().getAway_team())){
            team2Players.add(player.getName());
        }



        if(s1!=0){
            Label team1Label = new Label("Team 1 Goal Scorers:");
            HBox hBox = new HBox(team1Label);
            team1Label.setStyle("-fx-text-fill: white;");
            pane.getChildren().add(hBox);
        }

        for (int i = 0; i < s1; i++) {
            ComboBox<String> scorerComboBox = new ComboBox<>();
            scorerComboBox.getItems().addAll(team1Players);
            scorerComboBox.setValue(Consts.OWN_GOAL);
            team1ScorerFields.add(scorerComboBox);
            HBox hBox = new HBox(new Label("Scorer " + (i + 1) + ":") , scorerComboBox);
            hBox.setId("layout");
            pane.getChildren().add(hBox);
        }

        int startRow = s1 + 1;

        if (s2!=0){
            Label team2Label = new Label("Team 2 Goal Scorers:");
            team2Label.setStyle("-fx-text-fill: white;");
            HBox hBox = new HBox(team2Label);
            pane.getChildren().add(hBox);
        }



        for (int i = 0; i < s2; i++) {
            ComboBox<String> scorerComboBox = new ComboBox<>();
            scorerComboBox.getItems().addAll(team2Players);
            scorerComboBox.setValue(Consts.OWN_GOAL);
            team2ScorerFields.add(scorerComboBox);
            HBox hBox = new HBox(new Label("Scorer " + (i + 1) + ":") , scorerComboBox);
            hBox.setId("layout");
            pane.getChildren().add(hBox);
        }

        Button submitButton = new Button("Submit Scorers");
        submitButton.setOnAction(e -> handleScorerSubmission(id, date, time, s1, s2, stdm, ref, gk1, gk2));

        pane.getChildren().add(new HBox(submitButton));

        pane.setId("vboxlayout");

        Scene nextScene = new Scene(pane);
        nextScene.getStylesheets().add("Drawable/style.css");
        window.setScene(nextScene);

    }

    private void handleScorerSubmission(int id, String date, String time, int s1, int s2, String stdm, String ref, String gk1, String gk2) {
        ArrayList<String> team1Scorers = new ArrayList<>();
        ArrayList<String> team2Scorers = new ArrayList<>();

        for (ComboBox<String> comboBox : team1ScorerFields) {
            team1Scorers.add(comboBox.getValue());
        }

        for (ComboBox<String> comboBox : team2ScorerFields) {
            team2Scorers.add(comboBox.getValue());
        }

        updateMatchInDatabase(id, date, time, s1, s2, stdm, ref, gk1, gk2, team1Scorers, team2Scorers);
    }

    private void updateMatchInDatabase(int id, String date, String time, int s1, int s2, String stdm, String ref,
            String gk1, String gk2, ArrayList<String> scores1, ArrayList<String> scores2) {

        MatchModel newmMatch = SharedModel.getSelected_match();

        String sql = "UPDATE Matches SET "
                + "date = ?, time = ?, ScoreTeam1 = ?, ScoreTeam2 = ?,"
                + " stadium_name = ?,match_referee = ?, GoalkeeperOfTeam1 = ?, GoalkeeperOfTeam2 = ?,"
                + " ScorersNames_Team1 = ?, ScorersNames_Team2 = ? WHERE id = ?";

        try {

            Connection con = Utils.SqliteConnection.Connector();
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setInt(3, s1);
            pstmt.setInt(4, s2);
            pstmt.setString(5, stdm);
            pstmt.setString(6, ref);
            pstmt.setString(7, gk1);
            pstmt.setString(8, gk2);
            pstmt.setString(9, String.join(",", scores1));
            pstmt.setString(10, String.join(",", scores2));
            pstmt.setInt(11, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {

                newmMatch.setDate(date);
                newmMatch.setTime(time);
                newmMatch.setScoreTeam1(s1);
                newmMatch.setScoreTeam2(s2);
                newmMatch.setMatch_referee(ref);
                newmMatch.setStadium_name(stdm);
                newmMatch.setGoalkeeperOfTeam1(gk1);
                newmMatch.setGoalkeeperOfTeam2(gk2);
                newmMatch.setScorersNames_Team1(scores1);
                newmMatch.setScorersNames_Team2(scores2);

                SharedModel.setSelected_match(newmMatch);
                updateTeamsInDB();

            } else {
                showAlert(Alert.AlertType.ERROR, "ERROR", "No details have been updated");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage().toString());
        }

    }

    private void updateTeamsInDB() {

        MatchModel match = SharedModel.getSelected_match();

        TeamModel team1 = DatabaseFunctions.getTeam(match.getHome_team());
        TeamModel team2 = DatabaseFunctions.getTeam(match.getAway_team());

        //played
        team1.setPlayed(team1.getPlayed() + 1);
        team2.setPlayed(team2.getPlayed() + 1);
        //gs gc
        team1.setGs(team1.getGs() + match.getScoreTeam1());
        team1.setGc(team1.getGc() + match.getScoreTeam2());

        team2.setGs(team2.getGs() + match.getScoreTeam2());
        team2.setGc(team2.getGc() + match.getScoreTeam1());

        // win draw lose points
        //draw
        if (match.getScoreTeam1() == match.getScoreTeam2()) {

            team1.setDraw(team1.getDraw() + 1);
            team1.setPoints(team1.getPoints() + 1);

            team2.setDraw(team2.getDraw() + 1);
            team2.setPoints(team2.getPoints() + 1);

        } //team1 win
        else if (match.getScoreTeam1() > match.getScoreTeam2()) {

            team1.setWin(team1.getWin() + 1);
            team1.setPoints(team1.getPoints() + 3);

            team2.setLose(team2.getLose() + 1);
        } //team2 win
        else {
            team2.setWin(team2.getWin() + 1);
            team2.setPoints(team2.getPoints() + 3);

            team1.setLose(team1.getLose() + 1);
        }

        String sql = "UPDATE TEAMS SET played=?,win=?,draw=?,lose =?,points=?, gs = ?, gc = ?  WHERE id = ?";

        try {
            Connection con = Utils.SqliteConnection.Connector();

            // Update first team
            PreparedStatement pstmt1 = con.prepareStatement(sql);
            pstmt1.setInt(1, team1.getPlayed());  // played
            pstmt1.setInt(2, team1.getWin());   // win
            pstmt1.setInt(3, team1.getDraw());   // draw
            pstmt1.setInt(4, team1.getLose());   // lose
            pstmt1.setInt(5, team1.getPoints());  // points
            pstmt1.setInt(6, team1.getGs());  // gs (goals scored)
            pstmt1.setInt(7, team1.getGc());   // gc (goals conceded)
            pstmt1.setInt(8, team1.getId());   // id
            pstmt1.executeUpdate();

            // Update second team
            PreparedStatement pstmt2 = con.prepareStatement(sql);
            pstmt2.setInt(1, team2.getPlayed());  // played
            pstmt2.setInt(2, team2.getWin());   // win
            pstmt2.setInt(3, team2.getDraw());   // draw
            pstmt2.setInt(4, team2.getLose());   // lose
            pstmt2.setInt(5, team2.getPoints());  // points
            pstmt2.setInt(6, team2.getGs());  // gs (goals scored)
            pstmt2.setInt(7, team2.getGc());   // gc (goals conceded)
            pstmt2.setInt(8, team2.getId());   // id
            pstmt2.executeUpdate();

            // Close the prepared statements
            pstmt1.close();
            pstmt2.close();

            // Close the connection
            con.close();

            updatePlayersInDB();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());

        }
    }

    private void updatePlayersInDB() {

        MatchModel match = SharedModel.getSelected_match();

        int s1 = match.getScoreTeam1();
        int s2 = match.getScoreTeam2();

        PlayerModel gk1 = DatabaseFunctions.getPlayer(match.getGoalkeeperOfTeam1());
        PlayerModel gk2 = DatabaseFunctions.getPlayer(match.getGoalkeeperOfTeam2());
        //gc
        gk1.setGoals_c(gk1.getGoals_c() + s2);
        gk2.setGoals_c(gk2.getGoals_c() + s1);
        //cleansheets
        if (s2 == 0) {
            gk1.setCleansheets(gk1.getCleansheets() + 1);
        }
        if (s1 == 0) {
            gk2.setCleansheets(gk2.getCleansheets() + 1);
        }

        String sql = "UPDATE Player SET goals_c = ?, cleansheets = ?  WHERE name = ?";
        try {
            Connection con = Utils.SqliteConnection.Connector();

            PreparedStatement pstmt1 = con.prepareStatement(sql);
            pstmt1.setInt(1, gk1.getGoals_c());
            pstmt1.setInt(2, gk1.getCleansheets());
            pstmt1.setString(3, gk1.getName());
            pstmt1.executeUpdate();

            PreparedStatement pstmt2 = con.prepareStatement(sql);
            pstmt2.setInt(1, gk2.getGoals_c());
            pstmt2.setInt(2, gk2.getCleansheets());
            pstmt2.setString(3, gk2.getName());
            pstmt2.executeUpdate();

            pstmt1.close();
            pstmt2.close();
            con.close();

            showAlert(Alert.AlertType.INFORMATION, "Update Success", "Match details have been updated.");
            window.close();
            afterClose();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage());

        }

        ArrayList<String> list = match.getScorersNames_Team1();
        list.addAll(match.getScorersNames_Team2());
        PlayerModel player;
        PreparedStatement pstmt;
        Connection con = Utils.SqliteConnection.Connector();
        if (!list.isEmpty()) {

            for (String name : list) {
                if (!name.equals(Consts.OWN_GOAL)){

                    player = DatabaseFunctions.getPlayer(name);

                    try {
                        pstmt = con.prepareStatement("UPDATE Player SET goals = ?  WHERE name = ?");
                        pstmt.setInt(1, player.getGoals()+1);
                        pstmt.setString(2, player.getName());
                        pstmt.executeUpdate();
                        pstmt.close();

                    } catch (SQLException e) {
                    }
                }


            }

        }

    }

    private void showAlert(Alert.AlertType type, String title, String content) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();
    }

    public abstract void afterClose();

}
