package at.ac.fhcampuswien.simplechattool.client;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class OnlineUsersPanel extends ListCell<String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        HBox node = new HBox();
        Circle online = new Circle(3);
        Text text = new Text(item);
        text.setStyle("-fx-font: 14 Calibri;");

        if (item != null) {
            online.setFill(Color.web("#0ad159"));
            node.setSpacing(5);
            node.setAlignment(Pos.CENTER_LEFT);
            node.getChildren().addAll(online, text);
            setGraphic(node);
        } else {
            setGraphic(null);
        }
    }
}
