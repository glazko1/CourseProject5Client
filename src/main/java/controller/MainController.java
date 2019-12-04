package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import main.Runner;
import util.SceneChanger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getNews();
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
        allUsers.setOnAction(event -> {
            allOrders.getScene().getWindow().hide();
            sceneChanger.changeScene("/fxml/users.fxml");
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

    private void getNews() {
        Runner.sendData(new ClientRequest("getAllNews", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            StringBuilder builder = new StringBuilder();
            Map<String, Object> userData = response.getData();
            List newsList = (List) userData.get("news");
            for (Map<String, Object> news : (List<Map<String, Object>>) newsList) {
                builder.append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date((long) news.get("newsDateTime"))));
                builder.append("\n");
                builder.append(news.get("newsText"));
                builder.append("\n");
                builder.append("\n");
            }
            news.setText(builder.toString());
        }
    }
}