package model;

public class OrderItem extends BaseEntity {
    private int quantity;
    private int unitPrice;
    private int temporaryAmount;
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

    public int getUnitPrice() { return unitPrice; }
    public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }

    public int getTemporaryAmount() { return temporaryAmount; }
    public void setTemporaryAmount(int temporaryAmount) { this.temporaryAmount = temporaryAmount; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    @Override
    public String toString() {
        return "OrderItem{dish=" + (dish != null ? dish.getName() : "N/A")
                + ", quantity=" + quantity + ", unitPrice=" + unitPrice
                + ", temporaryAmount=" + temporaryAmount + "}";
    }
}
