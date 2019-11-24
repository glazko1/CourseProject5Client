package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.User;
import entity.UserStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import main.Runner;
import util.MapParser;
import util.SceneChanger;

import java.util.HashMap;
import java.util.Map;

public class ProfileController {

    @FXML
    private ImageView avatar;

    @FXML
    private Button changeAvatar;

    @FXML
    private Text name;

    @FXML
    private Text username;

    @FXML
    private Text status;

    @FXML
    private Text email;

    @FXML
    private Button changePassword;

    @FXML
    private Button changeEmail;

    @FXML
    private Button basket;

    @FXML
    private Button back;

    @FXML
    private Button main;

    private MapParser parser = MapParser.getInstance();
    private User user;

    @FXML
    private void initialize() {
        fillInformation();
        changeAvatar.setOnAction(event -> SceneChanger.getInstance().changeScene("/fxml/change-avatar.fxml"));
        changePassword.setOnAction(event -> SceneChanger.getInstance().changeScene("/fxml/change-password.fxml"));
        changeEmail.setOnAction(event -> SceneChanger.getInstance().changeScene("/fxml/change-email.fxml"));
        back.setOnAction(event -> {
            back.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
        basket.setOnAction(event -> {
            basket.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/basket.fxml");
        });
        main.setOnAction(event -> {
            main.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
    }

    private void getUser() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Runner.getUserId());
        Runner.sendData(new ClientRequest("getUser", map));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> userMap = response.getData();
            user = parser.user((Map<String, Object>) userMap.get("user"));
        }
    }

    private void fillInformation() {
        getUser();
        avatar.setImage(new Image(getClass().getResourceAsStream("/icons/" + user.getAvatarNumber() + ".png")));
        name.setText(user.getFirstName() + " " + user.getLastName());
        username.setText(user.getUsername());
        UserStatus userStatus = user.getUserStatus();
        status.setText(userStatus.getStatusName());
        email.setText(user.getEmail());
    }
}
