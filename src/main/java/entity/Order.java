package entity;

import java.util.Date;
import java.util.List;

public class Order {

    private int orderId;
    private User user;
    private OrderStatus orderStatus;
    private Address address;
    private Date orderDate;
    private double orderSum;
    private List<Product> products;

    public Order(int orderId, User user, OrderStatus orderStatus,
                 Address address, Date orderDate, double orderSum, List<Product> products) {
        this.orderId = orderId;
        this.user = user;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderDate = orderDate;
        this.orderSum = orderSum;
        this.products = products;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(double orderSum) {
        this.orderSum = orderSum;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}