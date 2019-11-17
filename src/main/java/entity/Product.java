package entity;

public class Product {

    private int productId;
    private String productName;
    private Department department;
    private String imagePath;
    private double price;
    private int amount;

    public Product(int productId, String productName, Department department, String imagePath, double price, int amount) {
        this.productId = productId;
        this.productName = productName;
        this.department = department;
        this.imagePath = imagePath;
        this.price = price;
        this.amount = amount;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}