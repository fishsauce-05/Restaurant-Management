package dao;

import dao.bill.BillDAO;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;

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
    @DisplayName("Lấy danh sách hóa đơn theo khoảng ngày")
    void testGetBillsByDateRange() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(2)
    @DisplayName("Lấy danh sách hóa đơn theo tháng")
    void testGetBillsByMonth() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(7, 2026);
        assertNotNull(bills);
    }

    @Test
    @Order(3)
    @DisplayName("Lấy hóa đơn theo khung giờ 11:00-13:00")
    void testGetBillsByTimeFrmLunch() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("11:00-13:00", "2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(4)
    @DisplayName("Lấy hóa đơn theo khung giờ 18:00-20:00")
    void testGetBillsByTimeFrmDinner() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("18:00-20:00", "2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(5)
    @DisplayName("Lấy hóa đơn theo khung giờ khác")
    void testGetBillsByTimeFrmOther() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("other", "2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(6)
    @DisplayName("Lấy hóa đơn theo ID không tồn tại")
    void testGetBillByIdNotFound() {
        Bill bill = billDAO.getBillById(-1);
        assertNull(bill);
    }

    @Test
    @Order(7)
    @DisplayName("Lấy hóa đơn theo ID bằng 0")
    void testGetBillByIdZero() {
        Bill bill = billDAO.getBillById(0);
        assertNull(bill);
    }

    @Test
    @Order(8)
    @DisplayName("Lấy hóa đơn với khoảng ngày đảo ngược trả về danh sách rỗng")
    void testGetBillsByDateRangeReversed() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2026-12-31", "2024-01-01");
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(9)
    @DisplayName("Lấy hóa đơn theo tháng không hợp lệ")
    void testGetBillsByInvalidMonth() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(0, 2026);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("Lấy hóa đơn theo tháng 13 không hợp lệ")
    void testGetBillsByMonth13() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(13, 2026);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(11)
    @DisplayName("Lấy hóa đơn với cùng ngày bắt đầu và kết thúc")
    void testGetBillsByDateRangeSameDay() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2026-07-14", "2026-07-14");
        assertNotNull(bills);
    }

    @Test
    @Order(12)
    @DisplayName("Lấy hóa đơn theo tháng trong tương lai trả về danh sách rỗng")
    void testGetBillsByFutureMonth() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(12, 2099);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }
}
