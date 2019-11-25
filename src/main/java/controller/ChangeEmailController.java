package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.Runner;
import util.validator.UserInformationValidator;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class ChangeEmailController {

    private UserInformationValidator validator = UserInformationValidator.getInstance();

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    private TextField newEmail;

    @FXML
    private void initialize() {
        save.setOnAction(event -> saveChanges());
        cancel.setOnAction(event -> cancel.getScene().getWindow().hide());
    }

    private void saveChanges() {
        if (validator.validate(newEmail.getText())) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", Runner.getUserId());
            map.put("email", newEmail.getText());
            Runner.sendData(new ClientRequest("changeEmail", map));
            ServerResponse response = Runner.getData();
            if (!response.isError()) {
                save.getScene().getWindow().hide();
                Alert alert = new Alert(INFORMATION, "Смена адреса эл. почты прошла успешно!");
                alert.show();
            } else {
                Alert alert = new Alert(ERROR, "Произошла ошибка!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(ERROR, "Введен некорректный адрес эл. почты!");
            alert.show();
        }
    }
}