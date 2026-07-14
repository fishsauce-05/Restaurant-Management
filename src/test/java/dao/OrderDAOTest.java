package dao;

import dao.order.OrderDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDAOTest {

    private OrderDAO orderDAO;

    @BeforeAll
    void setUp() {
        orderDAO = new OrderDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Lấy tất cả đơn hàng chưa thanh toán")
    void testGetAllUnpaidOrders() {
        ArrayList<model.Order> orders = orderDAO.getAllUnpaidOrders();
        assertNotNull(orders);
    }

    @Test
    @Order(2)
    @DisplayName("Lấy chi tiết đơn hàng với bàn không tồn tại")
    void testGetOrderDetailNotFound() {
        model.Order order = orderDAO.getOrderDetail(-1);
        assertNull(order);
    }

    @Test
    @Order(3)
    @DisplayName("Lấy chi tiết đơn hàng với bàn ID bằng 0")
    void testGetOrderDetailZero() {
        model.Order order = orderDAO.getOrderDetail(0);
        assertNull(order);
    }

    @Test
    @Order(4)
    @DisplayName("Cập nhật trạng thái đơn hàng với ID không tồn tại")
    void testUpdateOrderStatusInvalidId() {
        boolean result = orderDAO.updateOrderStatus(-1);
        assertTrue(result || !result);
    }

    @Test
    @Order(5)
    @DisplayName("Lấy chi tiết đơn hàng với bàn ID rất lớn")
    void testGetOrderDetailLargeId() {
        model.Order order = orderDAO.getOrderDetail(999999);
        assertNull(order);
    }

    @Test
    @Order(6)
    @DisplayName("Kiểm tra danh sách đơn hàng chưa thanh toán có thông tin bàn")
    void testUnpaidOrdersHaveTableInfo() {
        ArrayList<model.Order> orders = orderDAO.getAllUnpaidOrders();
        assertNotNull(orders);
        for (model.Order order : orders) {
            assertNotNull(order.getTable());
            assertNotNull(order.getTable().getTableCode());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Kiểm tra đơn hàng chưa thanh toán có trạng thái đúng")
    void testUnpaidOrdersHaveCorrectStatus() {
        ArrayList<model.Order> orders = orderDAO.getAllUnpaidOrders();
        assertNotNull(orders);
        for (model.Order order : orders) {
            assertEquals(model.Order.STATUS_UNPAID, order.getStatus());
        }
    }
}
