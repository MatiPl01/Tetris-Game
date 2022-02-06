package tetris.game.others;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Dialog {
    public static void informationDialog(String title, String header, String content) {
        Alert informationDialog = new Alert(Alert.AlertType.INFORMATION);
        informationDialog.setTitle(title);
        informationDialog.setHeaderText(header);
        informationDialog.setContentText(content);
        informationDialog.showAndWait();
    }

    public static Optional<ButtonType> confirmationDialog(String title, String header) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle(title);
        confirmationDialog.setHeaderText(header);
        return confirmationDialog.showAndWait();
    }
}
