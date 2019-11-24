package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MapParser {

    private static final MapParser INSTANCE = new MapParser();

    public static MapParser getInstance() {
        return INSTANCE;
    }

    private MapParser() {}

    private ObjectMapper mapper = new ObjectMapper();

    public User user(Map<String, Object> map) {
        return new User((int) map.get("userId"), (String) map.get("username"), (String) map.get("firstName"),
                (String) map.get("lastName"), userStatus((Map<String, Object>) map.get("userStatus")),
                (String) map.get("email"), (boolean) map.get("banned"), (int) map.get("avatarNumber"));
    }

    public UserStatus userStatus(Map<String, Object> map) {
        return new UserStatus((int) map.get("statusId"), (String) map.get("statusName"));
    }

    public List<Product> products(List productData) {
        List<Product> products = new ArrayList<>();
        for (Map<String, Object> product : (List<Map<String, Object>>) productData) {
            products.add(product(product));
        }
        return products;
    }

    public Product product(Map<String, Object> map) {
        return new Product((int) map.get("productId"), (String) map.get("productName"),
                department((Map<String, Object>) map.get("department")), (String) map.get("imagePath"),
                (double) map.get("price"), (int) map.get("amount"));
    }

    public List<Department> departments(List departmentData) {
        List<Department> departments = new ArrayList<>();
        for (Map<String, Object> department : (List<Map<String, Object>>) departmentData) {
            departments.add(department(department));
        }
        return departments;
    }

    public Department department(Map<String, Object> map) {
        return new Department((int) map.get("departmentId"), (String) map.get("departmentName"));
    }

    public OrderStatus orderStatus(Map<String, Object> map) {
        return new OrderStatus((int) map.get("statusId"), (String) map.get("statusName"));
    }

    public Address address(Map<String, Object> map) {
        return new Address((int) map.get("addressId"), (String) map.get("region"),
                (String) map.get("locality"), (String) map.get("street"), (int) map.get("house"),
                (int) map.get("flat"));
    }

    public List<Order> orders(List orderData) {
        List<Order> orders = new ArrayList<>();
        for (Map<String, Object> order : (List<Map<String, Object>>) orderData) {
            orders.add(order(order));
        }
        return orders;
    }

    public Order order(Map<String, Object> map) {
        return new Order((int) map.get("orderId"), user((Map<String, Object>) map.get("user")),
                orderStatus((Map<String, Object>) map.get("orderStatus")),
                address((Map<String, Object>) map.get("address")), new Date((long) map.get("orderDate")),
                (double) map.get("orderSum"), products((List) map.get("products")));
    }
}