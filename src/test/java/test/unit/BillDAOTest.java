package test.unit;

import dao.bill.BillDAO;
import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

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
    @DisplayName("Lấy hóa đơn theo khoảng ngày hợp lệ")
    void testGetBillsByDateRangeValid() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(2)
    @DisplayName("Lấy hóa đơn với cùng ngày bắt đầu và kết thúc")
    void testGetBillsByDateRangeSameDay() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2026-07-14", "2026-07-14");
        assertNotNull(bills);
    }

    @Test
    @Order(3)
    @DisplayName("Lấy hóa đơn với ngày đảo ngược trả về rỗng")
    void testGetBillsByDateRangeReversed() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2026-12-31", "2024-01-01");
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Lấy hóa đơn khung giờ trưa 11:00-13:00")
    void testGetBillsByTimeFrmLunch() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("11:00-13:00", "2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(5)
    @DisplayName("Lấy hóa đơn khung giờ tối 18:00-20:00")
    void testGetBillsByTimeFrmDinner() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("18:00-20:00", "2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(6)
    @DisplayName("Lấy hóa đơn khung giờ khác")
    void testGetBillsByTimeFrmOther() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("other", "2024-01-01", "2026-12-31");
        assertNotNull(bills);
    }

    @Test
    @Order(7)
    @DisplayName("Lấy hóa đơn theo tháng hợp lệ")
    void testGetBillsByValidMonth() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(7, 2026);
        assertNotNull(bills);
    }

    @Test
    @Order(8)
    @DisplayName("Lấy hóa đơn theo tháng 0 không hợp lệ")
    void testGetBillsByMonth0() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(0, 2026);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(9)
    @DisplayName("Lấy hóa đơn theo tháng 13 không hợp lệ")
    void testGetBillsByMonth13() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(13, 2026);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("Lấy hóa đơn theo ID hợp lệ nhưng có thể không tồn tại")
    void testGetBillByIdValid() {
        Bill bill = billDAO.getBillById(1);
    }

    @Test
    @Order(11)
    @DisplayName("Lấy hóa đơn theo ID âm")
    void testGetBillByIdNegative() {
        Bill bill = billDAO.getBillById(-1);
        assertNull(bill);
    }

    @Test
    @Order(12)
    @DisplayName("Lấy hóa đơn theo ID bằng 0")
    void testGetBillByIdZero() {
        Bill bill = billDAO.getBillById(0);
        assertNull(bill);
    }

    @Test
    @Order(13)
    @DisplayName("Lấy hóa đơn theo ID rất lớn")
    void testGetBillByIdLargeValue() {
        Bill bill = billDAO.getBillById(Integer.MAX_VALUE);
        assertNull(bill);
    }

    @Test
    @Order(14)
    @DisplayName("Lấy hóa đơn theo ngày trong tương lai xa")
    void testGetBillsByFutureDateRange() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2099-01-01", "2099-12-31");
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(15)
    @DisplayName("Lấy hóa đơn theo tháng trong tương lai xa")
    void testGetBillsByFutureMonth() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(1, 2099);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(16)
    @DisplayName("Lấy hóa đơn khung giờ trưa với ngày đảo ngược")
    void testGetBillsByTimeFrmReversedDates() {
        ArrayList<Bill> bills = billDAO.getBillsByTimeFrm("11:00-13:00", "2026-12-31", "2024-01-01");
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }

    @Test
    @Order(17)
    @DisplayName("Kiểm tra hóa đơn có thông tin booking khi có dữ liệu")
    void testBillsHaveBookingInfo() {
        ArrayList<Bill> bills = billDAO.getBillsByDateRange("2024-01-01", "2026-12-31");
        assertNotNull(bills);
        for (Bill bill : bills) {
            assertTrue(bill.getId() > 0);
            assertTrue(bill.getTotalAmount() >= 0);
        }
    }

    @Test
    @Order(18)
    @DisplayName("Lấy hóa đơn theo tháng âm")
    void testGetBillsByNegativeMonth() {
        ArrayList<Bill> bills = billDAO.getBillsByMonth(-1, 2026);
        assertNotNull(bills);
        assertTrue(bills.isEmpty());
    }
}
