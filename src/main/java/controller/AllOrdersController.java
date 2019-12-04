package controller;

import cooperation.ClientRequest;
import cooperation.ServerResponse;
import entity.Order;
import entity.property.OrderProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import main.Runner;
import util.MapParser;
import util.SceneChanger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class AllOrdersController {

    @FXML
    private TableView<OrderProperty> orderTable;

    @FXML
    private TableColumn<OrderProperty, String> userColumn;

    @FXML
    private TableColumn<OrderProperty, String> dateTimeColumn;

    @FXML
    private TableColumn<OrderProperty, String> statusColumn;

    @FXML
    private TableColumn<OrderProperty, String> addressColumn;

    @FXML
    private TableColumn<OrderProperty, Double> sumColumn;

    @FXML
    private Button orderInfo;

    @FXML
    private Button processOrder;

    @FXML
    private Button basket;

    @FXML
    private Button back;

    @FXML
    private Button main;

    private List<Order> orders;
    private MapParser parser = MapParser.getInstance();

    @FXML
    private void initialize() {
        fillOrderTable();
        orderInfo.setOnAction(event -> showOrderDetails());
        processOrder.setOnAction(event -> {
            processOrder();
            fillOrderTable();
        });
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

    private void showOrderDetails() {
        OrderProperty orderProperty = orderTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/order.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OrderController controller = loader.getController();
        controller.setOrder(orderProperty);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private void getOrders() {
        Runner.sendData(new ClientRequest("getAllOrders", new HashMap<>()));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Map<String, Object> orderMap = response.getData();
            List orderData = (List) orderMap.get("orders");
            orders = parser.orders(orderData);
        }
    }

    private void fillOrderTable() {
        getOrders();
        List<OrderProperty> orderProperties = new ArrayList<>();
        orders.forEach(order -> orderProperties.add(new OrderProperty(order)));
        orderTable.setItems(FXCollections.observableArrayList(orderProperties));
        userColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(dateFormat.format(cellData.getValue().getOrderDate())));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderStatus().getStatusName()));
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getAddress().getRegion() + " обл., " +
                        cellData.getValue().getAddress().getLocality() + ", " +
                        cellData.getValue().getAddress().getStreet() + ", " +
                        cellData.getValue().getAddress().getHouse() + "-" +
                        cellData.getValue().getAddress().getFlat()));
        sumColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getOrderSum()).asObject());
        showButtons(null);
        orderTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showButtons(newValue));
    }

    private void processOrder() {
        OrderProperty orderProperty = orderTable.getSelectionModel().getSelectedItem();
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderProperty.getOrderId());
        Runner.sendData(new ClientRequest("processOrder", map));
        ServerResponse response = Runner.getData();
        if (!response.isError()) {
            Alert alert = new Alert(INFORMATION, "Заказ успешно обработан!");
            alert.show();
        } else {
            Alert alert = new Alert(ERROR, "Произошла ошибка!");
            alert.show();
        }
    }

    private void showButtons(OrderProperty orderProperty) {
        if (orderProperty == null) {
            orderInfo.setVisible(false);
            processOrder.setVisible(false);
        } else {
            orderInfo.setVisible(true);
            if ("В обработке".equals(orderProperty.getOrderStatus().getStatusName())) {
                processOrder.setVisible(true);
            } else {
                processOrder.setVisible(false);
            }
        }
    }
}