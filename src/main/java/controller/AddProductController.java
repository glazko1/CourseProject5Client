package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.Department;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.Runner;
import util.ImageSender;
import util.MapParser;
import util.validator.ProductInformationValidator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class AddProductController {

    @FXML
    private Button addProduct;

    @FXML
    private Button cancel;

    @FXML
    private TextField productName;

    @FXML
    private ChoiceBox<Department> department;

    @FXML
    private Button uploadFile;

    @FXML
    private TextField price;

    @FXML
    private TextField amount;

    @FXML
    private Text filePath;

    private MapParser parser = MapParser.getInstance();
    private ProductInformationValidator validator = ProductInformationValidator.getInstance();
    private ImageSender imageSender = ImageSender.getInstance();
    private File image;

    @FXML
    private void initialize() {
        fillDepartmentsBox();
        addProduct.setOnAction(event -> addProduct());
        cancel.setOnAction(event -> cancel.getScene().getWindow().hide());
        uploadFile.setOnAction(event -> showFileChooser());
    }

    private void addProduct() {
        String productNameText = productName.getText();
        String priceText = price.getText();
        String filePathText = filePath.getText();
        if (validator.validate(productNameText, priceText, filePathText)) {
            Map<String, Object> data = new HashMap<>();
            data.put("productName", productNameText);
            data.put("departmentId", department.getValue().getDepartmentId());
            data.put("price", Double.parseDouble(priceText));
            Runner.sendData(new ClientRequest("addProduct", data));
            ServerResponse response = Runner.getData();
            if (!response.isError()) {
                addProduct.getScene().getWindow().hide();
                try {
                    imageSender.sendImage(image, productNameText);
                } catch (IOException e) {
                    Alert alert = new Alert(ERROR, "Ошибка при загрузке файла!");
                    alert.show();
                }
                ServerResponse imageResponse = Runner.getData();
                if (!imageResponse.isError()) {
                    Alert alert = new Alert(INFORMATION, "Продукт успешно добавлен!");
                    alert.show();
                } else {
                    Alert alert = new Alert(ERROR, "Ошибка при загрузке файла!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(ERROR, "Информация некорректна!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(ERROR, "Введенные данные некорректны! Пожалуйста, повторите ввод.");
            alert.show();
        }
    }

    private void fillDepartmentsBox() {
        Runner.sendData(new ClientRequest("getAllDepartments", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> departmentMap = response.getData();
            List departmentData = (List) departmentMap.get("departments");
            List<Department> departments = parser.departments(departmentData);
            department.setItems(FXCollections.observableArrayList(departments));
        }
    }

    private void showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение товара");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            image = file;
            filePath.setText(image.getAbsolutePath());
        }
    }
}