package entity;

public class Product {

    private int productId;
    private String productName;
    private Department department;
    private String imagePath;
    private double price;

    public Product(int productId, String productName, Department department, String imagePath, double price) {
        this.productId = productId;
        this.productName = productName;
        this.department = department;
        this.imagePath = imagePath;
        this.price = price;
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
}