/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Models.PlayerModel;
import Models.StatsModel;
import Utils.DatabaseFunctions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 *
 * @author abdo
 */
public class TopStatDesign {

    public static VBox display(String title, String imgUrl, ArrayList<StatsModel> list) {

        VBox vBox = new VBox();
        vBox.setId("main_layout");

        HBox header = new HBox();
        header.setId("header");

        Label h_label = new Label(title);

        ImageView header_imgVeiw = new ImageView(imgUrl);
        header_imgVeiw.setFitHeight(64);
        header_imgVeiw.setFitWidth(64);
        header.getChildren().addAll(h_label, header_imgVeiw);
        header.setMinWidth(400);
        
        String team1 = DatabaseFunctions.getTeamName(list.get(0).getTeam());
        String team2 = DatabaseFunctions.getTeamName(list.get(1).getTeam());
        String team3 = DatabaseFunctions.getTeamName(list.get(2).getTeam());
        

        vBox.getChildren().addAll(header,
                createHBox("1st", list.get(0).getName(), list.get(0).getValue() + "",DatabaseFunctions.getTeamImage(team1)),
                createHBox("2nd", list.get(1).getName(), list.get(1).getValue() + "",DatabaseFunctions.getTeamImage(team2)),
                createHBox("3rd", list.get(2).getName(), list.get(2).getValue() + "",DatabaseFunctions.getTeamImage(team3))
        );

        vBox.getStylesheets().add("Drawable/top_style.css");

        return vBox;
    }

    private static HBox createHBox(String index, String nameText, String valueText , String logo) {

        Label indexLabel = new Label(index);
        Label nameLabel = new Label(nameText);
        Label valueLabel = new Label(valueText);
        
        File file = new File(logo);
        Image image = new Image(file.toURI().toString(), 36, 36, true, true);
        ImageView imageView = new ImageView(image);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox = new HBox();
        hbox.setId("layout");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(indexLabel, nameLabel, spacer,imageView, valueLabel);

        return hbox;
    }

}
