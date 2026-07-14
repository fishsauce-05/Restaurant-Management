package dao;

import dao.dish.DishDAO;
import model.Dish;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DishDAOTest {

    private DishDAO dishDAO;
    private int testDishId;
    private final String TEST_DISH_CODE = "TEST_" + System.currentTimeMillis();

    @BeforeAll
    void setUp() {
        dishDAO = new DishDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Thêm món ăn mới")
    void testAddDish() {
        Dish dish = new Dish();
        dish.setDishCode(TEST_DISH_CODE);
        dish.setName("Món Test DAO");
        dish.setCategory("Khai vị");
        dish.setPrice(50000);
        dish.setDescription("Món ăn dùng cho test");
        boolean result = dishDAO.addDish(dish);
        assertTrue(result);
        assertTrue(dish.getId() > 0);
        testDishId = dish.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Thêm món ăn với mã trùng lặp")
    void testAddDishDuplicateCode() {
        Dish dish = new Dish();
        dish.setDishCode(TEST_DISH_CODE);
        dish.setName("Món trùng mã");
        dish.setCategory("Khai vị");
        dish.setPrice(30000);
        boolean result = dishDAO.addDish(dish);
        assertFalse(result);
    }

    @Test
    @Order(3)
    @DisplayName("Thêm món ăn null")
    void testAddDishNull() {
        boolean result = dishDAO.addDish(null);
        assertFalse(result);
    }

    @Test
    @Order(4)
    @DisplayName("Thêm món ăn với giá bằng 0")
    void testAddDishZeroPrice() {
        Dish dish = new Dish();
        dish.setDishCode("ZERO_PRICE_" + System.currentTimeMillis());
        dish.setName("Món giá 0");
        dish.setCategory("Khai vị");
        dish.setPrice(0);
        boolean result = dishDAO.addDish(dish);
        assertFalse(result);
    }

    @Test
    @Order(5)
    @DisplayName("Lấy tất cả món ăn")
    void testGetAllDishes() {
        ArrayList<Dish> dishes = dishDAO.getAllDishes();
        assertNotNull(dishes);
        assertFalse(dishes.isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("Tìm kiếm món ăn theo tên")
    void testSearchDish() {
        ArrayList<Dish> dishes = dishDAO.searchDish("Món Test");
        assertNotNull(dishes);
        assertFalse(dishes.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Tìm kiếm món ăn theo mã hoặc tên")
    void testSearchDishes() {
        ArrayList<Dish> dishes = dishDAO.searchDishes(TEST_DISH_CODE);
        assertNotNull(dishes);
        assertFalse(dishes.isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("Lấy món ăn theo danh mục")
    void testGetDishesByCategory() {
        ArrayList<Dish> dishes = dishDAO.getDishesByCategory("Khai vị");
        assertNotNull(dishes);
        assertFalse(dishes.isEmpty());
    }

    @Test
    @Order(9)
    @DisplayName("Lấy món ăn theo mã")
    void testGetDishByCode() {
        Dish dish = dishDAO.getDishByCode(TEST_DISH_CODE);
        assertNotNull(dish);
        assertEquals(TEST_DISH_CODE, dish.getDishCode());
        assertEquals("Món Test DAO", dish.getName());
    }

    @Test
    @Order(10)
    @DisplayName("Lấy món ăn theo mã không tồn tại")
    void testGetDishByCodeNotFound() {
        Dish dish = dishDAO.getDishByCode("NONEXISTENT_CODE");
        assertNull(dish);
    }

    @Test
    @Order(11)
    @DisplayName("Kiểm tra mã món ăn tồn tại")
    void testIsDishCodeExists() {
        assertTrue(dishDAO.isDishCodeExists(TEST_DISH_CODE));
    }

    @Test
    @Order(12)
    @DisplayName("Kiểm tra mã món ăn không tồn tại")
    void testIsDishCodeNotExists() {
        assertFalse(dishDAO.isDishCodeExists("NONEXISTENT_CODE"));
    }

    @Test
    @Order(13)
    @DisplayName("Kiểm tra mã món ăn null")
    void testIsDishCodeExistsNull() {
        assertFalse(dishDAO.isDishCodeExists(null));
    }

    @Test
    @Order(14)
    @DisplayName("Cập nhật món ăn")
    void testUpdateDish() {
        Dish dish = dishDAO.getDishByCode(TEST_DISH_CODE);
        assertNotNull(dish);
        dish.setName("Món Test Cập Nhật");
        dish.setPrice(75000);
        boolean result = dishDAO.updateDish(dish);
        assertTrue(result);

        Dish updated = dishDAO.getDishByCode(TEST_DISH_CODE);
        assertNotNull(updated);
        assertEquals("Món Test Cập Nhật", updated.getName());
        assertEquals(75000, updated.getPrice(), 0.01);
    }

    @Test
    @Order(15)
    @DisplayName("Cập nhật món ăn null")
    void testUpdateDishNull() {
        boolean result = dishDAO.updateDish(null);
        assertFalse(result);
    }

    @Test
    @Order(16)
    @DisplayName("Cập nhật món ăn với ID không hợp lệ")
    void testUpdateDishInvalidId() {
        Dish dish = new Dish();
        dish.setId(0);
        dish.setDishCode("X");
        dish.setName("X");
        dish.setCategory("X");
        dish.setPrice(1);
        boolean result = dishDAO.updateDish(dish);
        assertFalse(result);
    }

    @Test
    @Order(17)
    @DisplayName("Tìm kiếm món ăn không tồn tại")
    void testSearchDishNotFound() {
        ArrayList<Dish> dishes = dishDAO.searchDish("NONEXISTENT_DISH_XYZ_12345");
        assertNotNull(dishes);
        assertTrue(dishes.isEmpty());
    }

    @Test
    @Order(18)
    @DisplayName("Kiểm tra checkDishCode")
    void testCheckDishCode() {
        assertTrue(dishDAO.checkDishCode(TEST_DISH_CODE));
        assertFalse(dishDAO.checkDishCode("NONEXISTENT"));
    }

    @Test
    @Order(19)
    @DisplayName("Kiểm tra món ăn đã được đặt chưa")
    void testHasDishBeenOrdered() {
        boolean result = dishDAO.hasDishBeenOrdered(testDishId);
        assertFalse(result);
    }

    @Test
    @Order(20)
    @DisplayName("Xóa món ăn test")
    void testDeleteDish() {
        boolean result = dishDAO.deleteDish(testDishId);
        assertTrue(result);
        Dish deleted = dishDAO.getDishByCode(TEST_DISH_CODE);
        assertNull(deleted);
    }

    @Test
    @Order(21)
    @DisplayName("Xóa món ăn với ID không hợp lệ")
    void testDeleteDishInvalidId() {
        boolean result = dishDAO.deleteDish(0);
        assertFalse(result);
    }

    @Test
    @Order(22)
    @DisplayName("Xóa món ăn với ID âm")
    void testDeleteDishNegativeId() {
        boolean result = dishDAO.deleteDish(-1);
        assertFalse(result);
    }
}
