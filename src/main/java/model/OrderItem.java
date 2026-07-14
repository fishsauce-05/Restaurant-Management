package model;

public class OrderItem extends BaseEntity {
    private int quantity;
    private double unitPrice;
    private double temporaryAmount;
    private Dish dish;

    public OrderItem() {
        super();
    }

    public OrderItem(Dish dish, int quantity) {
        super();
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = dish.getPrice();
        this.temporaryAmount = quantity * unitPrice;
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getTemporaryAmount() { return temporaryAmount; }
    public void setTemporaryAmount(double temporaryAmount) { this.temporaryAmount = temporaryAmount; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    @Override
    public String toString() {
        return "OrderItem{dish=" + (dish != null ? dish.getName() : "N/A")
                + ", quantity=" + quantity + ", unitPrice=" + unitPrice
                + ", temporaryAmount=" + temporaryAmount + "}";
    }
}
