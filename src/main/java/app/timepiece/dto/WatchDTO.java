package app.timepiece.dto;

public class WatchDTO {

    private Long id;
    private String name;
    private String status;
    private String description;
    private double price;
    private String brandName;
    private int yearProduced;
    private String model;
    private String material;
    private String watchStrap;
    private String size;
    private String accessories;
    private String referenceCode;
    private String watchTypeName;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getYearProduced() {
        return yearProduced;
    }

    public void setYearProduced(int yearProduced) {
        this.yearProduced = yearProduced;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getWatchStrap() {
        return watchStrap;
    }

    public void setWatchStrap(String watchStrap) {
        this.watchStrap = watchStrap;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getWatchTypeName() {
        return watchTypeName;
    }

    public void setWatchTypeName(String watchTypeName) {
        this.watchTypeName = watchTypeName;
    }
}