package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import main.Runner;
import util.SceneChanger;

public class MainController {

    @FXML
    private Button myOrders;

    @FXML
    private Button catalog;

    @FXML
    private TextArea news;

    @FXML
    private Button myProfile;

    @FXML
    private Button allUsers;

    @FXML
    private Button allOrders;

    @FXML
    private Button basket;

    @FXML
    private Button back;

    private SceneChanger sceneChanger = SceneChanger.getInstance();

    @FXML
    private void initialize() {
        if (!"Администратор".equals(Runner.getStatus().getStatusName())) {
            allOrders.setVisible(false);
            allUsers.setVisible(false);
        }
        myOrders.setOnAction(event -> {
            myOrders.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/my-orders.fxml");
        });
        catalog.setOnAction(event -> {
            catalog.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/catalog.fxml");
        });
        myProfile.setOnAction(event -> {
            catalog.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/profile.fxml");
        });
        allOrders.setOnAction(event -> {
            allOrders.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/all-orders.fxml");
        });
        basket.setOnAction(event -> {
            basket.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/basket.fxml");
        });
        back.setOnAction(event -> {
            Runner.setStatus(null);
            Runner.setUserId(-1);
            Runner.setUsername(null);
            back.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/index.fxml");
        });
    }
}