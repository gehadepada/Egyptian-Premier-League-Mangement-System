package Forms;

import Models.PlayerModel;
import Models.TeamModel;
import Utils.DatabaseFunctions;
import Utils.SharedModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class UpdateTeamForm {

    private Stage window;
    private static ImageView imageView = new ImageView();
    private static Rectangle background = new Rectangle(128, 128, Color.LIGHTGRAY);
    private static String url = "";

    public void display(TeamModel selectedTeam) {

        ArrayList<String> players = new ArrayList<>();

        for(PlayerModel playerModel : DatabaseFunctions.getTeamPlayers(SharedModel.getSelected_team().getId())){
            players.add(playerModel.getName());
        }

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setOnCloseRequest(event -> afterClose());

        TextField nameField = new TextField(selectedTeam.getName());
        TextField playedField = new TextField(String.valueOf(selectedTeam.getPlayed()));
        TextField winField = new TextField(String.valueOf(selectedTeam.getWin()));
        TextField drawField = new TextField(String.valueOf(selectedTeam.getDraw()));
        TextField loseField = new TextField(String.valueOf(selectedTeam.getLose()));
        TextField gsField = new TextField(String.valueOf(selectedTeam.getGs()));
        TextField gcField = new TextField(String.valueOf(selectedTeam.getGc()));
        TextField difField = new TextField(String.valueOf(selectedTeam.getDif()));
        TextField pointsField = new TextField(String.valueOf(selectedTeam.getPoints()));
        ComboBox capitainComboBox = new ComboBox();
        capitainComboBox.getItems().setAll(players);
        capitainComboBox.setValue(players.get(0));

        url = selectedTeam.getImage();

        Label image_label = new Label("Team Logo");

        HBox layout1 = new HBox();
        HBox layout2 = new HBox();

        layout1.setId("layout");
        layout2.setId("layout");

        Button importButton = new Button("Import New Image");

        imageView.setFitHeight(128);
        imageView.setFitWidth(128);
        imageView.setPreserveRatio(true);

        StackPane imageContainer = new StackPane();
        imageContainer.getChildren().addAll(background, imageView);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            updateTeamInDatabase(nameField.getText().toString() , capitainComboBox.getValue().toString());

        });

        try {

            File file = new File(url);
            Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);

        } catch (Exception e) {
        }

        importButton.setOnAction(event -> importImage(window));

        layout1.getChildren().addAll(image_label, imageContainer);
        layout2.getChildren().addAll(importButton, saveButton);

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(15));

        HBox hbox = new HBox(10, new Label("Capitan Name :"), capitainComboBox);
        hbox.setId("layout");

        formLayout.getChildren().addAll(
                createLabeledField("Name:", nameField, true),
                hbox,
                createLabeledField("Played:", playedField, false),
                createLabeledField("Win:", winField, false),
                createLabeledField("Draw:", drawField, false),
                createLabeledField("Lose:", loseField, false),
                createLabeledField("Goals Scored:", gsField, false),
                createLabeledField("Goals Conceded:", gcField, false),
                createLabeledField("Points:", pointsField, false),
                layout1,
                layout2
        );

        Scene scene = new Scene(formLayout);
        scene.getStylesheets().add("Drawable/style.css");
        window.setTitle("Update Team Details");
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

    private void updateTeamInDatabase(String Name , String captn) {

        TeamModel new_Team = SharedModel.getSelected_team();
        
        String sql = "UPDATE TEAMS SET name = ?, image = ? ,capitan =? WHERE id = ?";

        try {
            Connection con = Utils.SqliteConnection.Connector();
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Name);
            pstmt.setString(2, url);
            pstmt.setString(3, captn);
            pstmt.setInt(4, new_Team.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                new_Team.setName(Name);
                new_Team.setImage(url);
                new_Team.setCapitan(captn);
                SharedModel.setSelected_team(new_Team);
                showAlert(Alert.AlertType.INFORMATION, "Update Success", "The team details have been updated.");
                window.close();
                afterClose();
            } else {
                showAlert(Alert.AlertType.ERROR, "ERROR", "No details have been updated");
            }
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

    private void importImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Image image = new Image(new FileInputStream(file));
                imageView.setImage(image);
                url = file.getPath();
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load image");
            }
        }
    }

    public abstract void afterClose();

}
