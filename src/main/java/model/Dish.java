package model;

public class Dish extends BaseEntity {
    private String dishCode;
    private String name;
    private String category;
    private double price;
    private String description;
    private boolean available;

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";

    public Dish() {
        super();
        setStatus(STATUS_ACTIVE);
        this.available = true;
    }

    public Dish(int id, String dishCode, String name, String category, double price) {
        super();
        setId(id);
        setStatus(STATUS_ACTIVE);
        this.dishCode = dishCode;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = true;
    }

    public String getDishCode() { return dishCode; }
    public void setDishCode(String dishCode) { this.dishCode = dishCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getCode() { return dishCode; }
    public void setCode(String code) { this.dishCode = code; }

    public double getCurrentPrice() { return price; }
    public void setCurrentPrice(double currentPrice) { this.price = currentPrice; }

    @Override
    public String toString() {
        return dishCode + " - " + name + " (" + category + ") - " + String.format("%,.0f VNĐ", price);
    }
}
