/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Models.PlayerModel;
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

/**
 *
 * @author abdo
 */
public abstract class CreatePlayerForm {

    private Stage window;

    public void display() {

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Player Form");
        window.setOnCloseRequest(event -> afterClose());

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setHgap(8);
        root.setVgap(10);

        Label name_label = new Label("Player Name");
        Label number_label = new Label("Player Number");
        Label age_label = new Label("Player Age");
        Label rank_label = new Label("Player Rank");
        Label type_label = new Label("Player postion");
        Label team_label = new Label("Team");

        Button save_btn = new Button("    Save    ");

        TextField name_entry = new TextField();
        TextField number_entry = new TextField();
        TextField rank_entry = new TextField();
        TextField age_entry = new TextField();
        ChoiceBox position_entry = new ChoiceBox<>();
        ChoiceBox teams_entry = new ChoiceBox<>();

        HBox layout1 = new HBox();
        HBox layout2 = new HBox();
        HBox layout3 = new HBox();
        HBox layout4 = new HBox();
        HBox layout5 = new HBox();
        HBox layout6 = new HBox();

        layout1.setId("layout");
        layout2.setId("layout");
        layout3.setId("layout");
        layout4.setId("layout");
        layout5.setId("layout");
        layout6.setId("layout");

        layout1.getChildren().addAll(name_label, name_entry);
        layout2.getChildren().addAll(age_label, age_entry);
        layout3.getChildren().addAll(number_label,number_entry );
        layout6.getChildren().addAll(rank_label,rank_entry );
        layout4.getChildren().addAll(type_label, position_entry);
        layout5.getChildren().addAll(team_label, teams_entry, save_btn);


        GridPane.setConstraints(layout1, 0, 0);
        GridPane.setConstraints(layout2, 0, 1);
        GridPane.setConstraints(layout3, 0, 2);
        GridPane.setConstraints(layout6, 0, 3);
        GridPane.setConstraints(layout4, 0, 4);
        GridPane.setConstraints(layout5, 0, 5);

        root.getChildren().addAll(layout1, layout2, layout3,layout6, layout4, layout5);

        position_entry.getItems().addAll(Consts.positions);
        position_entry.setValue(Consts.positions[0]);

        ArrayList<String> teams = new ArrayList();
        teams.addAll(DatabaseFunctions.getAllTeamsNames());

        teams_entry.getItems().addAll(teams);
        teams_entry.setValue(teams.get(0));

        save_btn.setOnAction(value -> {
            String name = name_entry.getText();
            String type = position_entry.getValue().toString();
            String team = teams_entry.getValue().toString();
            String age = age_entry.getText();
            String number = number_entry.getText();
            String rank = rank_entry.getText();
            
            Check(name, type,team,age,number,rank);
        });

        Scene scene = new Scene(root);
        scene.getStylesheets().add("Drawable/style.css");
        window.setScene(scene);

        window.show();

    }

    private  void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();
    }

    private  void Check(String name, String type,String team, String age,String number,String rank) {

        if (name.isEmpty() || number.isEmpty() || age.isEmpty()||rank.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please Fill all Fields");
        } else {

            try {

                int p_age = Integer.parseInt(age);
                int p_no = Integer.parseInt(number);
                int p_rank = Integer.parseInt(rank);
                int team_id = DatabaseFunctions.getTeamID(team);

                PlayerModel model = new PlayerModel(name, type, team_id, p_age, p_no,p_rank);
                showAlert(Alert.AlertType.INFORMATION, "Done", "Player added Successfully");
                window.close();
                afterClose();

            } catch (SQLException ex) {

                showAlert(Alert.AlertType.ERROR, "Error", ex.getLocalizedMessage());

            }
        }
    }

    public abstract void afterClose();

}
