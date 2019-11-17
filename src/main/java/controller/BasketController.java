package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.Product;
import entity.property.ProductProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import main.Runner;
import util.MapParser;
import util.SceneChanger;

import java.util.*;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class BasketController {

    @FXML
    private TableView<ProductProperty> productTable;

    @FXML
    private TableColumn<ProductProperty, String> nameColumn;

    @FXML
    private TableColumn<ProductProperty, String> departmentColumn;

    @FXML
    private TableColumn<ProductProperty, String> numberColumn;

    @FXML
    private ImageView image;

    @FXML
    private Text name;

    @FXML
    private Text price;

    @FXML
    private Button add;

    @FXML
    private Text sum;

    @FXML
    private Button remove;

    @FXML
    private Button back;

    @FXML
    private Button main;

    @FXML
    private Button makeOrder;

    private List<Product> products;
    private Map<ProductProperty, Integer> productProperties;
    private MapParser parser = MapParser.getInstance();

    @FXML
    private void initialize() {
        fillProductTable();
        add.setOnAction(event -> addProduct());
        remove.setOnAction(event -> removeProduct());
        back.setOnAction(event -> {
            back.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
        main.setOnAction(event -> {
            main.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
        makeOrder.setOnAction(event -> {
            if (checkProductsAmount()) {
                SceneChanger.getInstance().changeSceneAndWait("/fxml/make-order.fxml");
                fillProductTable();
            } else {
                Alert alert = new Alert(ERROR, "Вы выбрали слишком большое количество товара! Повторите попытку позже.");
                alert.show();
            }
        });
    }

    private void getProducts() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Runner.getUserId());
        Runner.sendData(new ClientRequest("getBasketProducts", map));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> productMap = response.getData();
            products = parser.products(productMap);
        }
    }

    private void fillProductTable() {
        getProducts();
        productProperties = new HashMap<>();
        products.forEach(product -> {
            ProductProperty productProperty = new ProductProperty(product);
            if (productProperties.containsKey(productProperty)) {
                productProperties.replace(productProperty, productProperties.get(productProperty) + 1);
            } else {
                productProperties.put(productProperty, 1);
            }
        });
        productTable.setItems(FXCollections.observableList(new ArrayList<>(productProperties.keySet())));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getDepartmentName()));
        numberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(productProperties.get(cellData.getValue()))));
        productTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showProductDetails(newValue));
        productTable.refresh();
        sum.setText(String.format("%.2f", products.stream().mapToDouble(Product::getPrice).sum()));
    }

    private void showProductDetails(ProductProperty productProperty) {
        if (productProperty != null) {
            name.setText(productProperty.getProductName());
            price.setText(String.format("%.2f", productProperty.getPrice()));
            image.setImage(new Image(productProperty.getImagePath()));
        } else {
            name.setText("");
            price.setText("");
            image.setImage(null);
        }
    }

    private void addProduct() {
        int productId = productTable.getSelectionModel()
                .selectedItemProperty()
                .getValue()
                .getProductId();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Runner.getUserId());
        map.put("productId", productId);
        Runner.sendData(new ClientRequest("addProductToBasket", map));
        Runner.getData();
        fillProductTable();
    }

    private void removeProduct() {
        int productId = productTable.getSelectionModel()
                .selectedItemProperty()
                .getValue()
                .getProductId();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Runner.getUserId());
        map.put("productId", productId);
        Runner.sendData(new ClientRequest("removeProductFromBasket", map));
        Runner.getData();
        fillProductTable();
    }

    private boolean checkProductsAmount() {
        final boolean[] correct = {true};
        productProperties.forEach((product, amount) -> {
            if (amount > product.getAmount()) {
                correct[0] = false;
            }
        });
        return correct[0];
    }
}