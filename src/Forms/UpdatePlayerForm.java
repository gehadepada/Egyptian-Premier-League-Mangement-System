package Forms;

import Models.PlayerModel;
import Models.TeamModel;
import Utils.Consts;
import Utils.DatabaseFunctions;
import Utils.PlayerType;
import Utils.SharedModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class UpdatePlayerForm {

    private Stage window;
    private static ImageView imageView = new ImageView();
    private static Rectangle background = new Rectangle(128, 128, Color.LIGHTGRAY);
    private static String url = "";

    public void display(PlayerModel selectedPlayer) {

        ArrayList<String> teams = DatabaseFunctions.getAllTeamsNames();
        teams.add(Consts.NO_TEAM);

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setOnCloseRequest(event -> afterClose());

        Label type_label = new Label("Player postion");
        Label team_label = new Label("Team");
        Label info_label = new Label("* if you choose No team Player will removed");

        ComboBox position_entry = new ComboBox<>();
        ComboBox teams_entry = new ComboBox<>();

        position_entry.getItems().setAll(Consts.positions);
        teams_entry.getItems().setAll(teams);

        position_entry.setValue(selectedPlayer.getType());
        position_entry.setDisable(true);
        teams_entry.setValue(DatabaseFunctions.getTeamName(selectedPlayer.getTeam()));

        TextField nameField = new TextField(selectedPlayer.getName());
        TextField ageField = new TextField(String.valueOf(selectedPlayer.getAge()));
        TextField numberField = new TextField(String.valueOf(selectedPlayer.getNumber()));
        TextField goalsField = new TextField(String.valueOf(selectedPlayer.getGoals()));
        TextField goalsCField;
        TextField cleanSheetsField;
        TextField rankField = new TextField(String.valueOf(selectedPlayer.getRank()));

        if (selectedPlayer.getType().equals(PlayerType.GoalKeeper.toString())) {
            goalsCField = new TextField(String.valueOf(selectedPlayer.getGoals_c()));
            cleanSheetsField = new TextField(String.valueOf(selectedPlayer.getCleansheets()));
        } else {
            goalsCField = new TextField("N/A");
            cleanSheetsField = new TextField("N/A");
        }

        Button saveButton = new Button("Update Player Information");

        saveButton.setOnAction(value -> {
            if (teams_entry.getValue().toString().equals(Consts.NO_TEAM)) {
                deletePlayerInDatabase(selectedPlayer.getName());
            } else {
                int age = Integer.parseInt(ageField.getText().toString());
                int no = Integer.parseInt(numberField.getText().toString());
                int id = DatabaseFunctions.getTeamID(teams_entry.getValue().toString());
                int rank = Integer.parseInt(rankField.getText().toString());
                updatePlayerInDatabase(age,no,id,rank);
            }
        });

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(15));

        HBox layout1 = new HBox();
        HBox layout2 = new HBox();
        HBox layout3 = new HBox();
        HBox layout4 = new HBox();

        layout1.setId("layout");
        layout2.setId("layout");
        layout4.setId("layout");
        layout3.setId("errlayout");

        layout4.setAlignment(Pos.CENTER);

        layout1.getChildren().addAll(team_label, teams_entry);
        layout2.getChildren().addAll(type_label, position_entry);
        layout3.getChildren().addAll(info_label);
        layout4.getChildren().addAll(saveButton);

        formLayout.getChildren().addAll(
                createLabeledField("Name:", nameField, false),
                layout1,
                layout2,
                createLabeledField("Age:", ageField, true),
                createLabeledField("Number:", numberField, true),
                createLabeledField("Goals:", goalsField, false),
                createLabeledField("Rank:", rankField, true),
                createLabeledField("Goals Conceded:", goalsCField, false),
                createLabeledField("Clean Sheets:", cleanSheetsField, false),
                layout3,
                layout4
        );

        Scene scene = new Scene(formLayout);
        scene.getStylesheets().add("Drawable/style.css");
        window.setTitle("Update Player Details");
        window.setScene(scene);
        window.show();
    }

    private HBox createLabeledField(String labelText, TextField textField, boolean editable) {
        Label label = new Label(labelText);
        textField.setEditable(editable);
        HBox hbox = new HBox(10, label, textField);
        hbox.setId("layout");
        return hbox;
    }

    private void updatePlayerInDatabase(int age , int no , int id ,int rank) {

        PlayerModel newPlayer = SharedModel.getSelected_player();

        String sql = "UPDATE Player SET age = ?, number = ? ,team_id =?,rank =? WHERE name = ?";

        try {
            
            Connection con = Utils.SqliteConnection.Connector();
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            pstmt.setInt(1, age);
            pstmt.setInt(2, no);
            pstmt.setInt(3, id);
            pstmt.setInt(4, rank);
            pstmt.setString(5, newPlayer.getName());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                newPlayer.setAge(age);
                newPlayer.setNumber(no);
                newPlayer.setTeam(id);
                newPlayer.setTeam(rank);
                SharedModel.setSelected_player(newPlayer);
                showAlert(Alert.AlertType.INFORMATION, "Update Success", "The Player details have been updated.");
                window.close();
                afterClose();
            } else {
                showAlert(Alert.AlertType.ERROR, "ERROR", "No details have been updated");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage().toString());
        }

    }

    private void deletePlayerInDatabase(String name) {

        String sql = "DELETE FROM Player WHERE name = ?";
        try {
            Connection con = Utils.SqliteConnection.Connector();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Delete Success", "Player Deleted Successfully");
            window.close();
            afterClose();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", e.getMessage().toString());
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
