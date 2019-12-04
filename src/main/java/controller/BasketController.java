package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.Product;
import entity.property.ProductProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private RadioButton rubles;

    @FXML
    private RadioButton dollars;

    @FXML
    private Button back;

    @FXML
    private Button main;

    @FXML
    private Button makeOrder;

    @FXML
    private Text discount;

    private List<Product> products;
    private Map<ProductProperty, Integer> productProperties;
    private MapParser parser = MapParser.getInstance();
    private boolean priceInRubles = true;

    @FXML
    private void initialize() {
        add.setVisible(false);
        remove.setVisible(false);
        fillProductTable();
        if (products.isEmpty()) {
            makeOrder.setVisible(false);
        }
        add.setOnAction(event -> addProduct());
        remove.setOnAction(event -> removeProduct());
        rubles.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                dollars.setSelected(false);
                priceInRubles = true;
                fillProductTable();
            }
        }));
        dollars.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                rubles.setSelected(false);
                priceInRubles = false;
                fillProductTable();
            }
        }));
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
            List productData = (List) productMap.get("products");
            products = parser.products(productData);
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
        double totalSum = products.stream().mapToDouble(Product::getPrice).sum();
        if (totalSum >= 500) {
            discount.setText("Скидка 10%");
            totalSum *= 0.9;
        } else if (totalSum >= 200) {
            discount.setText("Скидка 5%");
            totalSum *= 0.95;
        } else {
            discount.setText("");
        }
        if (priceInRubles) {
            sum.setText(String.format("%.2f р.", totalSum));
        } else {
            sum.setText(String.format("$%.2f", totalSum / 2.1));
        }
    }

    private void showProductDetails(ProductProperty productProperty) {
        if (productProperty != null) {
            name.setText(productProperty.getProductName());
            double productPrice = productProperty.getPrice();
            if (priceInRubles) {
                price.setText(String.format("%.2f р.", productPrice));
            } else {
                price.setText(String.format("$%.2f", productPrice / 2.1));
            }
            image.setImage(new Image(productProperty.getImagePath()));
            if (productProperty.getAmount() > productProperties.get(productProperty)) {
                add.setVisible(true);
            }
            remove.setVisible(true);
        } else {
            name.setText("");
            price.setText("");
            image.setImage(null);
            add.setVisible(false);
            remove.setVisible(false);
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
        if (products.isEmpty()) {
            makeOrder.setVisible(false);
        }
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
        if (products.isEmpty()) {
            makeOrder.setVisible(false);
        }
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