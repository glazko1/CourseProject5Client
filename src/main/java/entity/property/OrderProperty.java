package entity.property;

import entity.*;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderProperty {

    private IntegerProperty orderId;
    private User user;
    private OrderStatus orderStatus;
    private Address address;
    private ObjectProperty<Date> orderDate;
    private DoubleProperty orderSum;
    private List<ProductProperty> products;

    public OrderProperty(Order order) {
        this.orderId = new SimpleIntegerProperty(order.getOrderId());
        this.user = order.getUser();
        this.orderStatus = order.getOrderStatus();
        this.address = order.getAddress();
        this.orderDate = new SimpleObjectProperty<>(order.getOrderDate());
        this.orderSum = new SimpleDoubleProperty(order.getOrderSum());
        this.products = new ArrayList<>();
        order.getProducts().forEach(product -> products.add(new ProductProperty(product)));
    }

    public int getOrderId() {
        return orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getOrderDate() {
        return orderDate.get();
    }

    public ObjectProperty<Date> orderDateProperty() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate.set(orderDate);
    }

    public double getOrderSum() {
        return orderSum.get();
    }

    public DoubleProperty orderSumProperty() {
        return orderSum;
    }

    public void setOrderSum(double orderSum) {
        this.orderSum.set(orderSum);
    }

    public List<ProductProperty> getProducts() {
        return products;
    }

    public void setProducts(List<ProductProperty> products) {
        this.products = products;
    }
}