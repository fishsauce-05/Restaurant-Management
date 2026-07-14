package test.unit;

import dao.dish.DishStatDAO;
import model.DishStat;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DishStatDAOTest {

    private DishStatDAO dishStatDAO;

    @BeforeAll
    void setUp() {
        dishStatDAO = new DishStatDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Lấy danh sách món bán chạy với khoảng ngày hợp lệ")
    void testGetBestSellingDishValid() {
        ArrayList<DishStat> dishes = dishStatDAO.getBestSellingDish("2024-01-01", "2026-12-31");
        assertNotNull(dishes);
    }

    @Test
    @Order(2)
    @DisplayName("Lấy danh sách món bán chạy với ngày trong tương lai")
    void testGetBestSellingDishFuture() {
        ArrayList<DishStat> dishes = dishStatDAO.getBestSellingDish("2099-01-01", "2099-12-31");
        assertNotNull(dishes);
        assertTrue(dishes.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Kết quả món bán chạy sắp xếp theo doanh thu giảm dần")
    void testBestSellingDishOrderedByRevenue() {
        ArrayList<DishStat> dishes = dishStatDAO.getBestSellingDish("2024-01-01", "2026-12-31");
        assertNotNull(dishes);
        for (int i = 1; i < dishes.size(); i++) {
            assertTrue(dishes.get(i - 1).getTotalRevenue() >= dishes.get(i).getTotalRevenue());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Lấy tổng doanh thu với khoảng ngày hợp lệ")
    void testGetTotalRevenueValid() {
        int revenue = dishStatDAO.getTotalRevenue("2024-01-01", "2026-12-31");
        assertTrue(revenue >= 0);
    }

    @Test
    @Order(5)
    @DisplayName("Lấy tổng doanh thu với ngày trong tương lai trả về 0")
    void testGetTotalRevenueFuture() {
        int revenue = dishStatDAO.getTotalRevenue("2099-01-01", "2099-12-31");
        assertEquals(0, revenue);
    }

    @Test
    @Order(6)
    @DisplayName("Lấy top 5 món bán chạy")
    void testGetTopSellingDishTop5() {
        ArrayList<DishStat> dishes = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 5);
        assertNotNull(dishes);
        assertTrue(dishes.size() <= 5);
    }

    @Test
    @Order(7)
    @DisplayName("Lấy top 0 món bán chạy trả về rỗng")
    void testGetTopSellingDishTop0() {
        ArrayList<DishStat> dishes = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 0);
        assertNotNull(dishes);
        assertTrue(dishes.isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("Lấy top N lớn hơn số lượng thực tế")
    void testGetTopSellingDishLargeN() {
        ArrayList<DishStat> all = dishStatDAO.getBestSellingDish("2024-01-01", "2026-12-31");
        ArrayList<DishStat> top = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 1000);
        assertNotNull(top);
        assertEquals(all.size(), top.size());
    }

    @Test
    @Order(9)
    @DisplayName("Lấy tổng doanh thu với ngày đảo ngược")
    void testGetTotalRevenueReversedDates() {
        int revenue = dishStatDAO.getTotalRevenue("2026-12-31", "2024-01-01");
        assertEquals(0, revenue);
    }

    @Test
    @Order(10)
    @DisplayName("Lấy danh sách món bán chạy với cùng ngày")
    void testGetBestSellingDishSameDay() {
        ArrayList<DishStat> dishes = dishStatDAO.getBestSellingDish("2026-07-14", "2026-07-14");
        assertNotNull(dishes);
    }

    @Test
    @Order(11)
    @DisplayName("Lấy top 1 món bán chạy")
    void testGetTopSellingDishTop1() {
        ArrayList<DishStat> dishes = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 1);
        assertNotNull(dishes);
        assertTrue(dishes.size() <= 1);
    }

    @Test
    @Order(12)
    @DisplayName("Lấy top 3 món bán chạy")
    void testGetTopSellingDishTop3() {
        ArrayList<DishStat> dishes = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 3);
        assertNotNull(dishes);
        assertTrue(dishes.size() <= 3);
    }

    @Test
    @Order(13)
    @DisplayName("Kiểm tra thông tin món bán chạy có đầy đủ")
    void testBestSellingDishHasCompleteInfo() {
        ArrayList<DishStat> dishes = dishStatDAO.getBestSellingDish("2024-01-01", "2026-12-31");
        assertNotNull(dishes);
        for (DishStat ds : dishes) {
            assertNotNull(ds.getName());
            assertNotNull(ds.getDishCode());
            assertTrue(ds.getTotalQuantity() > 0);
            assertTrue(ds.getTotalRevenue() > 0);
            assertTrue(ds.getPrice() >= 0);
        }
    }

    @Test
    @Order(14)
    @DisplayName("Lấy tổng doanh thu với cùng ngày")
    void testGetTotalRevenueSameDay() {
        int revenue = dishStatDAO.getTotalRevenue("2026-07-14", "2026-07-14");
        assertTrue(revenue >= 0);
    }

    @Test
    @Order(15)
    @DisplayName("Lấy top 10 món bán chạy")
    void testGetTopSellingDishTop10() {
        ArrayList<DishStat> dishes = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 10);
        assertNotNull(dishes);
        assertTrue(dishes.size() <= 10);
    }

    @Test
    @Order(16)
    @DisplayName("Top món bán chạy cũng sắp xếp theo doanh thu giảm dần")
    void testTopSellingDishOrdered() {
        ArrayList<DishStat> dishes = dishStatDAO.getTopSellingDish("2024-01-01", "2026-12-31", 5);
        assertNotNull(dishes);
        for (int i = 1; i < dishes.size(); i++) {
            assertTrue(dishes.get(i - 1).getTotalRevenue() >= dishes.get(i).getTotalRevenue());
        }
    }
}
