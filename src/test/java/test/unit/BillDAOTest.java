package test.unit;

import dao.bill.BillDAO;
import model.Bill;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillDAOTest {

    private BillDAO billDAO;

    @BeforeAll
    void setUp() {
        billDAO = new BillDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Lấy hóa đơn theo ID hợp lệ nhưng có thể không tồn tại")
    void testGetBillByIdValid() {
        Bill bill = billDAO.getBillById(1);
    }

    @Test
    @Order(2)
    @DisplayName("Lấy hóa đơn theo ID âm")
    void testGetBillByIdNegative() {
        Bill bill = billDAO.getBillById(-1);
        assertNull(bill);
    }

    @Test
    @Order(3)
    @DisplayName("Lấy hóa đơn theo ID bằng 0")
    void testGetBillByIdZero() {
        Bill bill = billDAO.getBillById(0);
        assertNull(bill);
    }

    @Test
    @Order(4)
    @DisplayName("Lấy hóa đơn theo ID rất lớn")
    void testGetBillByIdLargeValue() {
        Bill bill = billDAO.getBillById(Integer.MAX_VALUE);
        assertNull(bill);
    }
}
