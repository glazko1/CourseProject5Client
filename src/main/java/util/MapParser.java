package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Department;
import entity.Product;
import entity.User;
import entity.UserStatus;

import java.util.ArrayList;
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
        return new User((int) map.get("userId"), map.get("username").toString(), map.get("firstName").toString(),
                map.get("lastName").toString(), userStatus((Map<String, Object>) map.get("userStatus")),
                map.get("email").toString(), (boolean) map.get("banned"), (int) map.get("avatarNumber"));
    }

    public UserStatus userStatus(Map<String, Object> map) {
        return new UserStatus((int) map.get("statusId"), map.get("statusName").toString());
    }

    public List<Product> products(Map<String, Object> map) {
        List<Product> products = new ArrayList<>();
        List productData = (List) map.get("products");
        for (Map<String, Object> product : (List<Map<String, Object>>) productData) {
            products.add(product(product));
        }
        return products;
    }

    public Product product(Map<String, Object> map) {
        return new Product((int) map.get("productId"), map.get("productName").toString(),
                department((Map<String, Object>) map.get("department")), map.get("imagePath").toString(),
                (double) map.get("price"));
    }

    public Department department(Map<String, Object> map) {
        return new Department((int) map.get("departmentId"), map.get("departmentName").toString());
    }
}