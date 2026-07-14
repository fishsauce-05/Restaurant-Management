package dao.order;

import model.Order;
import java.util.ArrayList;

public interface IOrderDAO {
    boolean addOrder(Order order);

    Order getOrderDetail(int tableId);

    ArrayList<Order> getAllUnpaidOrders();

    boolean updateOrderStatus(int orderId);
}
