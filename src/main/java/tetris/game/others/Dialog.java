package tetris.game.others;

import javafx.scene.control.Alert;

public class Dialog {
    public static void informationDialog(String title, String header, String content) {
        Alert informationDialog = new Alert(Alert.AlertType.INFORMATION);
        informationDialog.setTitle(title);
        informationDialog.setHeaderText(header);
        informationDialog.setContentText(content);
        informationDialog.showAndWait();
    }
}
