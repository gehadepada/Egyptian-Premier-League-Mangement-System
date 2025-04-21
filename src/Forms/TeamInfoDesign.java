/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Models.StatsModel;
import Models.TeamModel;
import Utils.DatabaseFunctions;
import Utils.SharedModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author abdo
 */
public class TeamInfoDesign {

    public static VBox display() {

        TeamModel team = SharedModel.getSelected_team();

        VBox main = new VBox();
        main.setId("main_layout2");

        VBox vBox = new VBox();
        vBox.setId("main_layout3");

        

        vBox.getChildren().addAll(
                createHBox("Team Name : " , team.getName()+" (Egypt)"),
                createHBox("Team Capitan : " , team.getCapitan()+" (C)"),
                createHBox("Games Played :",team.getPlayed()+""),
                createHBox("Goals Scored :",team.getGs()+""),
                createHBox("Goals Conceded :",team.getGc()+"")
        );

        main.getChildren().addAll(vBox);
        main.setAlignment(Pos.TOP_CENTER);

        main.getStylesheets().add("Drawable/top_style.css");

        return main;
    }

    private static HBox createHBox(String nameText, String valueText) {

        Label nameLabel = new Label(nameText);
        Label valueLabel = new Label(valueText);

        HBox hbox = new HBox();
        hbox.setId("layout3");
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(nameLabel, valueLabel);

        return hbox;
    }

}
