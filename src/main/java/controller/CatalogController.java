package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.Product;
import entity.property.ProductProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import main.Runner;
import util.MapParser;
import util.SceneChanger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogController {

    @FXML
    private TableView<ProductProperty> productTable;

    @FXML
    private TableColumn<ProductProperty, String> nameColumn;

    @FXML
    private TableColumn<ProductProperty, String> departmentColumn;

    @FXML
    private Button addProduct;

    @FXML
    private Button editProduct;

    @FXML
    private Button deleteProduct;

    @FXML
    private Button refresh;

    @FXML
    private ImageView image;

    @FXML
    private Text name;

    @FXML
    private Text price;

    @FXML
    private Button toBasket;

    @FXML
    private Slider priceMax;

    @FXML
    private Button categories;

    @FXML
    private Button filter;

    @FXML
    private Button basket;

    @FXML
    private Button back;

    @FXML
    private Button main;

    private List<Product> products;
    private MapParser parser = MapParser.getInstance();

    @FXML
    private void initialize() {
        if ("User".equals(Runner.getStatus().getStatusName())) {
            addProduct.setVisible(false);
            editProduct.setVisible(false);
            deleteProduct.setVisible(false);
        }
        fillProductTable();
        priceMax.valueProperty().addListener((observable, oldValue, newValue) -> countFilteredProducts(newValue.doubleValue()));
        basket.setOnAction(event -> {
            basket.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/basket.fxml");
        });
    }

    private void getProducts() {
        Runner.sendData(new ClientRequest("getAllProducts", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> productMap = response.getData();
            products = parser.products(productMap);
        }
    }

    private void fillProductTable() {
        getProducts();
        List<ProductProperty> productProperties = new ArrayList<>();
        products.forEach(product -> productProperties.add(new ProductProperty(product)));
        productTable.setItems(FXCollections.observableArrayList(productProperties));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getDepartmentName()));
        showProductDetails(null);
        productTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showProductDetails(newValue));
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

    private void countFilteredProducts(double price) {
        int number = (int) products.stream()
                .filter(product -> product.getPrice() < price)
                .count();
        filter.setText(filter.getText().replaceAll("[0-9]+", String.valueOf(number)));
    }

    private void filterProducts(double price) {
        List<ProductProperty> productProperties = new ArrayList<>();
        products.stream()
                .filter(product -> product.getPrice() < price)
                .forEach(product -> productProperties.add(new ProductProperty(product)));
        productTable.setItems(FXCollections.observableArrayList(productProperties));
    }
}
