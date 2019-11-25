package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Runner;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class ChangeAvatarController {

    @FXML
    private ImageView avatar;

    @FXML
    private Button previous;

    @FXML
    private Button next;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    private int avatarPosition = 1;

    @FXML
    private void initialize() {
        previous.setVisible(false);
        avatar.setImage(new Image(getClass().getResourceAsStream("/icons/1.png")));
        previous.setOnAction(event -> changeAvatar(false));
        next.setOnAction(event -> changeAvatar(true));
        save.setOnAction(event -> saveChanges());
        cancel.setOnAction(event -> cancel.getScene().getWindow().hide());
    }

    private void changeAvatar(boolean moveToNext) {
        if (moveToNext) {
            avatarPosition++;
        } else {
            avatarPosition--;
        }
        if (avatarPosition == 1) {
            previous.setVisible(false);
        } else if (avatarPosition == 10) {
            next.setVisible(false);
        } else {
            previous.setVisible(true);
            next.setVisible(true);
        }
        avatar.setImage(new Image(getClass().getResourceAsStream("/icons/" + avatarPosition + ".png")));
    }

    private void saveChanges() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Runner.getUserId());
        map.put("avatarNumber", avatarPosition);
        Runner.sendData(new ClientRequest("changeAvatar", map));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            save.getScene().getWindow().hide();
            Alert alert = new Alert(INFORMATION, "Смена аватара прошла успешно!");
            alert.show();
        } else {
            Alert alert = new Alert(ERROR, "Произошла ошибка!");
            alert.show();
        }
    }
}