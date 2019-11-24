package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.Department;
import entity.Product;
import entity.property.ProductProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import main.Runner;
import util.MapParser;
import util.SceneChanger;

import java.util.*;

public class CatalogController {

    @FXML
    private TableView<ProductProperty> productTable;

    @FXML
    private TableColumn<ProductProperty, String> nameColumn;

    @FXML
    private TableColumn<ProductProperty, String> departmentColumn;

    @FXML
    private TableColumn<ProductProperty, Double> priceColumn;

    @FXML
    private Button addProduct;

    @FXML
    private Button editProduct;

    @FXML
    private Button deleteProduct;

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
    private AnchorPane categoriesPane;

    @FXML
    private CheckBox category1;

    @FXML
    private CheckBox category2;

    @FXML
    private CheckBox category3;

    @FXML
    private CheckBox category4;

    @FXML
    private CheckBox category5;

    @FXML
    private Button basket;

    @FXML
    private Button back;

    @FXML
    private Button main;

    private SceneChanger sceneChanger = SceneChanger.getInstance();
    private List<Product> products;
    private List<String> departments;
    private MapParser parser = MapParser.getInstance();

    @FXML
    private void initialize() {
        if ("User".equals(Runner.getStatus().getStatusName())) {
            addProduct.setVisible(false);
            editProduct.setVisible(false);
            deleteProduct.setVisible(false);
        }
        fillProductTable();
        addProduct.setOnAction(event -> {
            sceneChanger.changeSceneAndWait("/fxml/add-product.fxml");
            fillProductTable();
        });
        toBasket.setOnAction(event -> addToBasket());
        priceMax.valueProperty().addListener((observable, oldValue, newValue) -> countFilteredProducts(newValue.doubleValue()));
        categories.setOnAction(event -> editCategoriesPane());
        filter.setOnAction(event -> filterProducts(priceMax.getValue()));
        basket.setOnAction(event -> {
            basket.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/basket.fxml");
        });
        back.setOnAction(event -> {
            back.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
        main.setOnAction(event -> {
            main.getScene().getWindow().hide();
            SceneChanger.getInstance().changeScene("/fxml/main.fxml");
        });
    }

    private void getProducts() {
        Runner.sendData(new ClientRequest("getAllProducts", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> productMap = response.getData();
            List productData = (List) productMap.get("products");
            products = parser.products(productData);
        }
    }

    private void fillProductTable() {
        getProducts();
        List<ProductProperty> productProperties = new ArrayList<>();
        products.forEach(product -> productProperties.add(new ProductProperty(product)));
        productTable.setItems(FXCollections.observableArrayList(productProperties));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getDepartmentName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
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

    private void addToBasket() {
        int productId = productTable.getSelectionModel()
                .selectedItemProperty()
                .getValue()
                .getProductId();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Runner.getUserId());
        map.put("productId", productId);
        Runner.sendData(new ClientRequest("addProductToBasket", map));
        Runner.getData();
    }

    private void countFilteredProducts(double price) {
        int number = (int) products.stream()
                .filter(product -> product.getPrice() < price)
                .filter(product -> departments.contains(product.getDepartment().getDepartmentName()))
                .count();
        filter.setText(filter.getText().replaceAll("[0-9]+", String.valueOf(number)));
    }

    private void filterProducts(double price) {
        List<ProductProperty> productProperties = new ArrayList<>();
        products.stream()
                .filter(product -> product.getPrice() < price)
                .filter(product -> departments.contains(product.getDepartment().getDepartmentName()))
                .forEach(product -> productProperties.add(new ProductProperty(product)));
        productTable.setItems(FXCollections.observableArrayList(productProperties));
    }

    private void editCategoriesPane() {
        List<CheckBox> checkBoxes = new ArrayList<>();
        Collections.addAll(checkBoxes, category1, category2, category3, category4, category5);
        if (categoriesPane.isVisible()) {
            categoriesPane.setVisible(false);
            departments = new ArrayList<>();
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    departments.add(checkBox.getText());
                }
                countFilteredProducts(priceMax.getValue());
                checkBox.setVisible(false);
                checkBox.setText("");
            }
        } else {
            int index = 0;
            for (Department department : getDepartments()) {
                CheckBox category = checkBoxes.get(index);
                category.setVisible(true);
                category.setText(department.getDepartmentName());
                ++index;
            }
            categoriesPane.setVisible(true);
        }
    }

    private List<Department> getDepartments() {
        Runner.sendData(new ClientRequest("getAllDepartments", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> departmentMap = response.getData();
            List departmentData = (List) departmentMap.get("departments");
            return parser.departments(departmentData);
        }
        return new ArrayList<>();
    }
}
