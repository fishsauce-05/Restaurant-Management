package model;

public class DishStat extends Dish {
    private int totalQuantity;
    private double totalRevenue;

    public DishStat() {
        super();
    }

    public DishStat(int totalQuantity, double totalRevenue) {
        super();
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    @Override
    public String toString() {
        return "DishStat{dish=" + getName() + " (" + getDishCode() + ")"
                + ", totalQuantity=" + totalQuantity
                + ", totalRevenue=" + String.format("%,.0f VNĐ", totalRevenue) + "}";
    }
}
