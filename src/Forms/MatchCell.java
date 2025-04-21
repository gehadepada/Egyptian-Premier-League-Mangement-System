package Forms;

import Models.MatchModel;
import Utils.DatabaseFunctions;
import java.io.File;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MatchCell extends ListCell<MatchModel> {

    private VBox content;
    private Label date_label;
    private Label score1_label,lbl, score2_label;
    private Label team1_Label, team2_Label;
    private Label stadium_Label , ref_Label;
    private Label played_label;
    private ImageView imgview1 = new ImageView();
    private ImageView imgview2 = new ImageView();

    public MatchCell() {
        super();

        date_label = new Label();
        score1_label = new Label();
        lbl = new Label();
        score2_label = new Label();
        team1_Label = new Label();
        team2_Label = new Label();
        stadium_Label = new Label();
        ref_Label = new Label();
        played_label = new Label();

        imgview1.setPreserveRatio(true);
        imgview1.setId("imgview");

        imgview2.setPreserveRatio(true);
        imgview2.setId("imgview");
        
        ImageView stdimgview = new ImageView("Drawable/stdm.png");
        stdimgview.setFitHeight(30);
        stdimgview.setFitWidth(30);
        
        ImageView refimgview = new ImageView("Drawable/whistle.png");
        refimgview.setFitHeight(30);
        refimgview.setFitWidth(30);
        
        HBox result = new HBox(score1_label,lbl,score2_label);
        
        VBox v1 = new VBox(imgview1,team1_Label);
        VBox v2 = new VBox(result,played_label);
        VBox v3 = new VBox(imgview2,team2_Label);

        HBox layout1 = new HBox(date_label);
        HBox layout2 = new HBox(v1,v2,v3);
        HBox layout3 = new HBox(stdimgview,stadium_Label);
        HBox layout4 = new HBox(refimgview,ref_Label);

        layout1.setId("hlayout");
        layout2.setId("hlayout");
        layout3.setId("hlayout");
        layout4.setId("hlayout");
        result.setId("hlayout");

        team1_Label.setId("teamname");
        team2_Label.setId("teamname");
        
        v1.setId("vlayout");
        v2.setId("vlayout");
        v3.setId("vlayout");

        content = new VBox(layout2,layout1);
        content.setId("content");
        
        content.setSpacing(10);
        content.getStylesheets().add("Drawable/match_style.css");
    }

    @Override
    protected void updateItem(MatchModel match, boolean empty) {
        super.updateItem(match, empty);
        if (empty || match == null) {
            setText(null);
            setGraphic(null);
        } else {

            date_label.setText(match.getDate());
            team1_Label.setText(DatabaseFunctions.getTeamName(match.getHome_team()));
            team2_Label.setText(DatabaseFunctions.getTeamName(match.getAway_team()));
            stadium_Label.setText(match.getStadium_name());
            ref_Label.setText(match.getMatch_referee());

            File file1 = new File(DatabaseFunctions.getTeamImage(team1_Label.getText().toString()));
            File file2 = new File(DatabaseFunctions.getTeamImage(team2_Label.getText().toString()));

            Image image1 = new Image(file1.toURI().toString(), 50, 50, true, true);
            Image image2 = new Image(file2.toURI().toString(), 50, 50, true, true);

            imgview1.setImage(image1);
            imgview2.setImage(image2);


            if (match.IsPlayed()) {
                played_label.setText("Ended");
                played_label.setId("nothield_label");
                score1_label.setText(String.valueOf(match.getScoreTeam1()));
                score2_label.setText(String.valueOf(match.getScoreTeam2()));
                lbl.setText("-");
            } else {
                played_label.setText("Not Played");
                played_label.setId("hield_label");
                score1_label.setText("");
                score2_label.setText("");
                lbl.setText(match.getTime());
            }

            setGraphic(content);
        }
    }
}
