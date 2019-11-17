package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import main.Runner;
import util.SceneChanger;
import util.validator.OrderInformationValidator;

import java.util.*;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class MakeOrderController {

    @FXML
    private Button makeOrder;

    @FXML
    private Button cancel;

    @FXML
    private TextField locality;

    @FXML
    private ChoiceBox<String> region;

    @FXML
    private TextField flat;

    @FXML
    private TextField street;

    @FXML
    private TextField house;

    private OrderInformationValidator validator = OrderInformationValidator.getInstance();

    @FXML
    private void initialize() {
        fillRegions();
        makeOrder.setOnAction(event -> makeOrder());
        cancel.setOnAction(event -> cancel.getScene().getWindow().hide());
    }

    private void fillRegions() {
        List<String> regions = new ArrayList<>();
        Collections.addAll(regions, "Минская", "Брестская", "Витебская",
                "Гомельская", "Гродненская", "Могилевская");
        region.setItems(FXCollections.observableArrayList(regions));
    }

    private void makeOrder() {
        String localityText = locality.getText();
        String streetText = street.getText();
        String houseText = house.getText();
        String flatText = flat.getText();
        if (localityText.isEmpty() || region == null || streetText.isEmpty() ||
                houseText.isEmpty() || flatText.isEmpty()) {
            Alert alert = new Alert(ERROR, "Одно или несколько обязательных полей не заполнены!");
            alert.show();
        } else if (validator.validate(localityText, streetText, houseText, flatText)) {
            String regionText = region.getValue();
            int houseNumber = Integer.parseInt(houseText);
            int flatNumber = Integer.parseInt(flatText);
            Map<String, Object> map = new HashMap<>();
            map.put("userId", Runner.getUserId());
            map.put("locality", localityText);
            map.put("region", regionText);
            map.put("street", streetText);
            map.put("house", houseNumber);
            map.put("flat", flatNumber);
            Runner.sendData(new ClientRequest("addOrder", map));
            ServerResponse response = Runner.getData();
            if (!response.isError()) {
                makeOrder.getScene().getWindow().hide();
                Alert alert = new Alert(INFORMATION, "Ваш заказ сохранен и будет обработан в ближайшее время! Спасибо за выбор нашего магазина.");
                alert.show();
            } else {
                Alert alert = new Alert(ERROR, "Ошибка при сохранении заказа! Повторите попытку позже.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(ERROR, "Информация некорректна!");
            alert.show();
        }
    }
}