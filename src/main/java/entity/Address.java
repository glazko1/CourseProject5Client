package entity;

public class Address {

    private int addressId;
    private String region;
    private String locality;
    private String street;
    private int house;
    private int flat;

    public Address(int addressId, String region,
                   String locality, String street, int house, int flat) {
        this.addressId = addressId;
        this.region = region;
        this.locality = locality;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouse() {
        return house;
    }

    public void setHouse(int house) {
        this.house = house;
    }

    public int getFlat() {
        return flat;
    }

    public void setFlat(int flat) {
        this.flat = flat;
    }
}