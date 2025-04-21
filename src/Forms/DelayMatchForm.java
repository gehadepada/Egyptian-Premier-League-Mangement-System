package Forms;

import Models.MatchModel;
import Models.PlayerModel;
import Models.TeamModel;
import Utils.Consts;
import Utils.DatabaseFunctions;
import Utils.SharedModel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class DelayMatchForm {

    private Stage window;
    Scene scene ;
    public void display(MatchModel selectedmatch) {

        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setOnCloseRequest(event -> afterClose());


        Label date_label = new Label("New Date");
        DatePicker date_picker = new DatePicker();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate date = LocalDate.parse(selectedmatch.getDate(), formatter);
        date_picker.setValue(date);
        date_picker.setEditable(false);


        Button ConfirmButton = new Button("Confirm");


        HBox layout1 = new HBox();
        HBox layout2 = new HBox();

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(15));


        layout1.getChildren().addAll(date_label, date_picker);
        layout2.getChildren().addAll(ConfirmButton);



        layout1.setId("hboxlayout3");
        layout2.setId("hboxlayout4");

        formLayout.getChildren().addAll(layout1, layout2);

        ConfirmButton.setOnAction(event -> {

            try {
                int id = selectedmatch.getId();
                String ndate = date_picker.getValue().toString();
                updateMatchInDatabase(id,ndate);

            } catch (NumberFormatException e) {

                showAlert(Alert.AlertType.ERROR, "ERROR", e.getLocalizedMessage());
            }

        });

        scene = new Scene(formLayout);
        scene.getStylesheets().add("Drawable/style.css");
        window.setTitle("Held Match");
        window.setScene(scene);
        window.show();
    }


    private void updateMatchInDatabase(int id, String date) {

        MatchModel newmMatch = SharedModel.getSelected_match();

        String sql = "UPDATE Matches SET date = ?, ishield = ? WHERE id = ?";

        try {

            Connection con = Utils.SqliteConnection.Connector();
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, date);
            pstmt.setInt(2, 1);
            pstmt.setInt(3, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {

                newmMatch.setDate(date);
                SharedModel.setSelected_match(newmMatch);
                showAlert(Alert.AlertType.INFORMATION, "Update Success", "Match details have been updated.");
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

    public abstract void afterClose();

}
