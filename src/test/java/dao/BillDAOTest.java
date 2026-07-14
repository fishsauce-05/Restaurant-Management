package dao;

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
    @DisplayName("Lấy hóa đơn theo ID không tồn tại")
    void testGetBillByIdNotFound() {
        Bill bill = billDAO.getBillById(-1);
        assertNull(bill);
    }

    @Test
    @Order(2)
    @DisplayName("Lấy hóa đơn theo ID bằng 0")
    void testGetBillByIdZero() {
        Bill bill = billDAO.getBillById(0);
        assertNull(bill);
    }
}
