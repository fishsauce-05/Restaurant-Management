package model;

public class DishStat extends Dish {
    private int totalQuantity;
    private int totalRevenue;

    public DishStat() {
        super();
    }

    public DishStat(int totalQuantity, int totalRevenue) {
        super();
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public int getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(int totalRevenue) { this.totalRevenue = totalRevenue; }

    @Override
    public String toString() {
        return "DishStat{dish=" + getName() + " (" + getDishCode() + ")"
                + ", totalQuantity=" + totalQuantity
                + ", totalRevenue=" + String.format("%,d VNĐ", totalRevenue) + "}";
    }
}
