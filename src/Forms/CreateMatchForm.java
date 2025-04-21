package Forms;

import Models.MatchModel;
import Utils.Consts;
import Utils.DatabaseFunctions;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

/**
 *
 * @author gehad
 */
public abstract class CreateMatchForm {

    private Stage window;

    public void display() {

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Match Form");

        window.setOnCloseRequest(event -> afterClose());

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setHgap(8);
        root.setVgap(10);
        
        ComboBox<Integer> hourComboBox = new ComboBox<>();
        for (int i = 1; i < 13; i++) {
            hourComboBox.getItems().add(i);
        }
        hourComboBox.setValue(7);

        ComboBox<Integer> minuteComboBox = new ComboBox<>();
        for (int i = 0; i < 60; i++) {
            minuteComboBox.getItems().add(i);
        }
        ComboBox<String> timetype = new ComboBox<>();
        timetype.getItems().addAll("PM","AM");
        timetype.setValue("PM");
        
        minuteComboBox.setValue(0);


        HBox timeBox = new HBox(10, hourComboBox, minuteComboBox,timetype);
        
        Label round_label = new Label("Match Round");
        Label team1_label = new Label("Home Team");
        Label team2_label = new Label("Away Team");
        Label date_label = new Label("Match Date");
        Label time_label = new Label("Match Time");
        Label referee_label = new Label("Match Referee");
        Label stadium_label = new Label("Stadium Name");

        Button save_btn = new Button("Save");

        ChoiceBox round_entry = new ChoiceBox<>();
        ChoiceBox team1_entry = new ChoiceBox<>();
        ChoiceBox team2_entry = new ChoiceBox<>();
        DatePicker date_entry = new DatePicker();
        TextField referee_entry = new TextField();
        TextField stadium_entry = new TextField();
        
        date_entry.setEditable(false);

        ArrayList<String> teams = new ArrayList();
        teams.addAll(DatabaseFunctions.getAllTeamsNames());

        team1_entry.getItems().addAll(teams);
        team1_entry.setValue(teams.get(0));

        team2_entry.getItems().addAll(teams);
        team2_entry.setValue(teams.get(0));
        
        
        round_entry.getItems().addAll(Consts.Rounds);
        round_entry.setValue(Consts.Rounds[0]);
        
        HBox layout1 = new HBox(round_label, round_entry);
        HBox layout2 = new HBox(team1_label, team1_entry);
        HBox layout3 = new HBox(team2_label, team2_entry);
        HBox layout4 = new HBox(date_label, date_entry);
        HBox layout5 = new HBox(referee_label, referee_entry);
        HBox layout6 = new HBox(stadium_label, stadium_entry, save_btn);
        HBox layout7 = new HBox(time_label,timeBox);
        

        layout1.setId("layout");
        layout2.setId("layout");
        layout3.setId("layout");
        layout4.setId("layout");
        layout5.setId("layout");
        layout6.setId("layout");
        layout7.setId("layout");

        root.getChildren().addAll(layout1, layout2, layout3, layout4,layout7, layout5,layout6);

        GridPane.setConstraints(layout1, 0, 0);
        GridPane.setConstraints(layout2, 0, 1);
        GridPane.setConstraints(layout3, 0, 2);
        GridPane.setConstraints(layout4, 0, 3);
         GridPane.setConstraints(layout7, 0,4);
        GridPane.setConstraints(layout5, 0, 5);
        GridPane.setConstraints(layout6, 0, 6);

        save_btn.setOnAction((ActionEvent value) -> {
            int round = Integer.parseInt(round_entry.getValue().toString());
            String team1 = team1_entry.getValue().toString();
            String team2 = team2_entry.getValue().toString();
            LocalDate date = date_entry.getValue();
            
            String hours =hourComboBox.getValue().toString();
            String minutes =minuteComboBox.getValue().toString();
            String type =timetype.getValue().toString();
            
            if(minutes.length()==1){
                minutes="0"+minutes;
            }
            
            
            String time = hours+ ":"+minutes+" "+type;
            String referee = referee_entry.getText();
            String stadium = stadium_entry.getText();
            try {
                Check(round,team1, team2, date, referee, stadium,time);
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add("Drawable/style.css");
        window.setScene(scene);
        window.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();
    }

    private void Check(int round ,String team1, String team2, LocalDate date, String referee, String stadium,String time) throws SQLException {
        if (date==null || referee.isEmpty() || stadium.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please Fill all Fields");
        } else if (team1.equals(team2)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please Enter Valid Teams");
        } else {
            MatchModel model = new MatchModel(round, DatabaseFunctions.getTeamID(team1), DatabaseFunctions.getTeamID(team2), date.toString(), referee, stadium,time);
            showAlert(Alert.AlertType.INFORMATION, "Done", "Match added Successfully");
            window.close();
            afterClose();

        }
    }
    
    public abstract void afterClose();
}
