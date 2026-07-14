package model;

public class OrderDish extends BaseEntity {
    private int quantity;
    private double currentPrice;
    private Dish dish;

    public OrderDish() {
        super();
    }

    public OrderDish(Dish dish, int quantity) {
        super();
        this.dish = dish;
        this.quantity = quantity;
        this.currentPrice = dish.getPrice();
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    public double getTemporaryAmount() {
        return this.quantity * this.currentPrice;
    }

    @Override
    public String toString() {
        return "OrderDish{dish=" + (dish != null ? dish.getName() : "N/A")
                + ", quantity=" + quantity
                + ", currentPrice=" + currentPrice
                + ", temporaryAmount=" + getTemporaryAmount() + "}";
    }
}
