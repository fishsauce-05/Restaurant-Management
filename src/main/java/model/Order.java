package model;

import java.util.ArrayList;
import java.util.Date;

public class Order extends BaseEntity {
    public static final String STATUS_UNPAID = "Chưa thanh toán";
    public static final String STATUS_PAID = "Đã thanh toán";

    private Date orderTime;
    private int totalAmount;
    private Table table;
    private User user;
    private ArrayList<OrderItem> orderItems;
    private ArrayList<OrderDish> orderDishes;

    public Order() {
        super();
        orderItems = new ArrayList<>();
        orderDishes = new ArrayList<>();
    }

    public Date getOrderTime() { return orderTime; }
    public void setOrderTime(Date orderTime) { this.orderTime = orderTime; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ArrayList<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(ArrayList<OrderItem> orderItems) { this.orderItems = orderItems; }

    public ArrayList<OrderDish> getOrderDishes() { return orderDishes; }
    public void setOrderDishes(ArrayList<OrderDish> orderDishes) { this.orderDishes = orderDishes; }

    public void addOrderDish(OrderDish od) {
        if (orderDishes == null) {
            orderDishes = new ArrayList<>();
        }
        orderDishes.add(od);

        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        OrderItem item = new OrderItem();
        item.setDish(od.getDish());
        item.setQuantity(od.getQuantity());
        item.setUnitPrice(od.getCurrentPrice());
        item.setTemporaryAmount(od.getQuantity() * od.getCurrentPrice());
        orderItems.add(item);

        recalculateTotal();
    }

    public void recalculateTotal() {
        int total = 0;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                item.setTemporaryAmount(item.getQuantity() * item.getUnitPrice());
                total += item.getTemporaryAmount();
            }
        }
        this.totalAmount = total;
    }

    @Override
    public String toString() {
        return "Order{id=" + getId() + ", table=" + (table != null ? table.getTableCode() : "N/A")
                + ", status='" + getStatus() + "', totalAmount=" + totalAmount + "}";
    }
}
