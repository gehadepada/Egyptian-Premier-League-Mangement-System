/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Models.TeamModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author abdo
 */
public abstract class CreateTeamForm {
    
    private static ImageView imageView = new ImageView();
    private static Rectangle background = new Rectangle(128, 128, Color.LIGHTGRAY);
    private static String url = "";
    private static Stage window;
    
    public void display(){
        
        window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Team Form");
        window.setOnCloseRequest(event -> afterClose());

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setHgap(8);
        root.setVgap(10);

        Label name_label = new Label("Team Name");
        Label image_label = new Label("Team Logo");
        
        Button save_btn = new Button("    Save    ");
        
        
        TextField name_entry = new TextField();
        
        HBox layout1 = new HBox();
        HBox layout2 = new HBox();
        HBox layout3 = new HBox();
        
        
        
        
        Button importButton = new Button("Import Image");
        importButton.setOnAction(event -> importImage(window));
        
        imageView.setFitHeight(128);
        imageView.setFitWidth(128);
        imageView.setPreserveRatio(true);
        
        StackPane imageContainer = new StackPane();
        imageContainer.getChildren().addAll(background, imageView);
        
        
        layout1.getChildren().addAll(name_label,name_entry);
        layout2.getChildren().addAll(image_label,imageContainer);
        layout3.getChildren().addAll(importButton,save_btn);
        
        
        GridPane.setConstraints(layout1, 0, 0);
        GridPane.setConstraints(layout2, 0, 1);
        GridPane.setConstraints(layout3, 0, 2);
        
        layout1.setId("layout");
        layout2.setId("layout");
        layout3.setId("layout");
        
        
        save_btn.setOnAction(event -> Check(name_entry.getText().toString(), url) );
       

        
        
        root.getChildren().addAll(layout1,layout2,layout3);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Drawable/style.css");
        window.setScene(scene);
        
        window.show();
        
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
                showAlert(AlertType.ERROR, "Error", "Could not load image");
            }
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.showAndWait();
    }
    
    private void Check(String name , String url){
        
        if(name.isEmpty() || url.isEmpty()){
            showAlert(AlertType.ERROR, "Error", "Please Fill all Fields");
        }
        
        else{
            
            try {
                
                TeamModel teamModel = new TeamModel(name , url);
                showAlert(AlertType.INFORMATION, "Done", "Team added Successfully");
                window.close();
                afterClose();
               
                
            } catch (SQLException ex) {
                
                showAlert(AlertType.ERROR, "Error", ex.getLocalizedMessage());
                
            }
        }
    }
    

    public abstract void afterClose();
    
    
}
