package controller;

import entity.Address;
import entity.property.OrderProperty;
import entity.property.ProductProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderController {

    @FXML
    private Text dateTime;

    @FXML
    private Text status;

    @FXML
    private Text address;

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
    private Text sum;

    private Map<ProductProperty, Integer> productProperties;

    @FXML
    private void initialize() {}

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

    public void setOrder(OrderProperty orderProperty) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateTime.setText(dateFormat.format(orderProperty.getOrderDate()));
        status.setText(orderProperty.getOrderStatus().getStatusName());
        Address orderAddress = orderProperty.getAddress();
        address.setText(orderAddress.getRegion() + " обл., " +
                orderAddress.getLocality() + ", " +
                orderAddress.getStreet() + ", " +
                orderAddress.getHouse() + "-" +
                orderAddress.getFlat());
        productProperties = new HashMap<>();
        orderProperty.getProducts().forEach(product -> {
            if (productProperties.containsKey(product)) {
                productProperties.replace(product, productProperties.get(product) + 1);
            } else {
                productProperties.put(product, 1);
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
        sum.setText(String.format("%.2f", orderProperty.getProducts().stream().mapToDouble(ProductProperty::getPrice).sum()));
    }
}