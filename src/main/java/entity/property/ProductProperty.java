package entity.property;

import entity.Department;
import entity.Product;
import javafx.beans.property.*;

import java.util.Objects;

public class ProductProperty {

    private IntegerProperty productId;
    private StringProperty productName;
    private Department department;
    private StringProperty imagePath;
    private DoubleProperty price;

    public ProductProperty(int productId, String productName, Department department, String imagePath, double price) {
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.department = department;
        this.imagePath = new SimpleStringProperty(imagePath);
        this.price = new SimpleDoubleProperty(price);
    }

    public ProductProperty(Product product) {
        this.productId = new SimpleIntegerProperty(product.getProductId());
        this.productName = new SimpleStringProperty(product.getProductName());
        this.department = product.getDepartment();
        this.imagePath = new SimpleStringProperty(product.getImagePath());
        this.price = new SimpleDoubleProperty(product.getPrice());
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductProperty that = (ProductProperty) o;
        return Objects.equals(productId.getValue(), that.productId.getValue()) &&
                Objects.equals(productName.getValue(), that.productName.getValue()) &&
                Objects.equals(department, that.department) &&
                Objects.equals(imagePath.getValue(), that.imagePath.getValue()) &&
                Objects.equals(price.getValue(), that.price.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId.getValue(), productName.getValue(), department, imagePath.getValue(), price.getValue());
    }

    @Override
    public String toString() {
        return "ProductProperty{" +
                "productId=" + productId +
                ", productName=" + productName +
                ", department=" + department +
                ", imagePath=" + imagePath +
                ", price=" + price +
                '}';
    }
}