package Forms;

import Models.TeamModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.File;

public class TeamTableCell extends TableCell<TeamModel, TeamModel> {
    private final HBox content;
    private final ImageView imageView;
    private final Label label;

    public TeamTableCell() {
        content = new HBox(10);
        imageView = new ImageView();
        label = new Label();

        imageView.setFitHeight(36);
        imageView.setFitWidth(36);
        content.getChildren().addAll(imageView, label);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(TeamModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            File file = new File(item.getImage());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            label.setText(item.getName());
            setGraphic(content);
        }
    }

    public static Callback<TableColumn<TeamModel, TeamModel>, TableCell<TeamModel, TeamModel>> forTableColumn() {
        return param -> new TeamTableCell();
    }
}
