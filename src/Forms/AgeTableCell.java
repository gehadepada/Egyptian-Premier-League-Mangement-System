package Forms;

import Models.AgesModel;
import Models.TeamModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.File;

public class AgeTableCell extends TableCell<AgesModel, AgesModel> {
    private final HBox content;
    private final ImageView imageView;
    private final Label label;

    public AgeTableCell() {
        content = new HBox(10);
        imageView = new ImageView();
        label = new Label();
        label.setStyle("-fx-text-fill: white;");

        imageView.setFitHeight(36);
        imageView.setFitWidth(36);
        content.getChildren().addAll(imageView, label);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(AgesModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            File file = new File(item.getTeam_image());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            label.setText(item.getTeam_name());
            setGraphic(content);
        }
    }

    public static Callback<TableColumn<AgesModel, AgesModel>, TableCell<AgesModel, AgesModel>> forTableColumn() {
        return param -> new AgeTableCell();
    }
}
