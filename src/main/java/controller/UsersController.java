package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.User;
import entity.property.UserProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Runner;
import util.MapParser;
import util.SceneChanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class UsersController {

    @FXML
    private TableView<UserProperty> userTable;

    @FXML
    private TableColumn<UserProperty, String> usernameColumn;

    @FXML
    private TableColumn<UserProperty, String> nameColumn;

    @FXML
    private TableColumn<UserProperty, String> surnameColumn;

    @FXML
    private TableColumn<UserProperty, String> statusColumn;

    @FXML
    private TableColumn<UserProperty, String> emailColumn;

    @FXML
    private TableColumn<UserProperty, String> bannedColumn;

    @FXML
    private Button ban;

    @FXML
    private Button changeToAdmin;

    @FXML
    private Button changeToUser;

    @FXML
    private Button basket;

    @FXML
    private Button back;

    @FXML
    private Button main;

    private List<User> users;
    private MapParser parser = MapParser.getInstance();

    @FXML
    private void initialize() {
        fillUserTable();
        ban.setOnAction(event -> {
            changeBanStatus();
            fillUserTable();
        });
        changeToAdmin.setOnAction(event -> {
            changeUserStatus(1);
            fillUserTable();
        });
        changeToUser.setOnAction(event -> {
            changeUserStatus(2);
            fillUserTable();
        });
        main.setOnAction(event -> {
            main.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
        basket.setOnAction(event -> {
            basket.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/basket.fxml");
        });
        back.setOnAction(event -> {
            back.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
    }

    private void getUsers() {
        Runner.sendData(new ClientRequest("getAllUsers", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> userMap = response.getData();
            List userData = (List) userMap.get("users");
            users = parser.users(userData);
        }
    }

    private void fillUserTable() {
        getUsers();
        List<UserProperty> userProperties = new ArrayList<>();
        users.forEach(user -> userProperties.add(new UserProperty(user)));
        userTable.setItems(FXCollections.observableArrayList(userProperties));
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserStatus().getStatusName()));
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        bannedColumn.setCellValueFactory(cellData -> {
            boolean banned = cellData.getValue().isBanned();
            return banned ? new SimpleStringProperty("Да") :
                    new SimpleStringProperty("Нет");
        });
        viewActionsForUser(null);
        userTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> viewActionsForUser(newValue));
    }

    private void changeBanStatus() {
        UserProperty user = userTable.getSelectionModel().getSelectedItem();
        int userId = user.getUserId();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        Runner.sendData(new ClientRequest("changeBanStatus", data));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            getUsers();
            Alert alert = new Alert(INFORMATION, "Статус блокировки пользователя изменен!");
            alert.show();
        } else {
            Alert alert = new Alert(ERROR, "Произошла ошибка!");
            alert.show();
        }
    }

    private void changeUserStatus(int status) {
        UserProperty user = userTable.getSelectionModel().getSelectedItem();
        int userId = user.getUserId();
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("statusId", status);
        Runner.sendData(new ClientRequest("changeUserStatus", data));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            getUsers();
            Alert alert = new Alert(INFORMATION, "Статус пользователя изменен!");
            alert.show();
        } else {
            Alert alert = new Alert(ERROR, "Произошла ошибка!");
            alert.show();
        }
    }

    private void viewActionsForUser(UserProperty user) {
        if (user != null) {
            if (user.isBanned()) {
                ban.setText("Разблокировать");
            } else {
                ban.setText("Заблокировать");
            }
            if ("Администратор".equals(user.getUserStatus().getStatusName())) {
                ban.setVisible(false);
                changeToAdmin.setVisible(false);
                changeToUser.setVisible(false);
            } else {
                ban.setVisible(true);
                changeToAdmin.setVisible(true);
                changeToUser.setVisible(false);
            }
        } else {
            ban.setVisible(false);
            changeToAdmin.setVisible(false);
            changeToUser.setVisible(false);
        }
    }
}

