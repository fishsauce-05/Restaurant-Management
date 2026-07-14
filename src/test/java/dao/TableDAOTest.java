package dao;

import dao.table.TableDAO;
import model.Table;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TableDAOTest {

    private TableDAO tableDAO;
    private int testTableId;
    private final String TEST_TABLE_CODE = "T_TEST_" + System.currentTimeMillis();

    @BeforeAll
    void setUp() {
        tableDAO = new TableDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Thêm bàn mới")
    void testAddTable() {
        Table table = new Table();
        table.setTableCode(TEST_TABLE_CODE);
        table.setName("Bàn Test DAO");
        table.setCapacity(4);
        table.setDescription("Bàn dùng cho test");
        boolean result = tableDAO.addTable(table);
        assertTrue(result);
        assertTrue(table.getId() > 0);
        testTableId = table.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Thêm bàn với mã trùng lặp")
    void testAddTableDuplicateCode() {
        Table table = new Table();
        table.setTableCode(TEST_TABLE_CODE);
        table.setName("Bàn trùng mã");
        table.setCapacity(2);
        boolean result = tableDAO.addTable(table);
        assertFalse(result);
    }

    @Test
    @Order(3)
    @DisplayName("Thêm bàn null")
    void testAddTableNull() {
        boolean result = tableDAO.addTable(null);
        assertFalse(result);
    }

    @Test
    @Order(4)
    @DisplayName("Thêm bàn với sức chứa bằng 0")
    void testAddTableZeroCapacity() {
        Table table = new Table();
        table.setTableCode("ZERO_CAP_" + System.currentTimeMillis());
        table.setName("Bàn 0 chỗ");
        table.setCapacity(0);
        boolean result = tableDAO.addTable(table);
        assertFalse(result);
    }

    @Test
    @Order(5)
    @DisplayName("Lấy tất cả bàn")
    void testGetAllTables() {
        ArrayList<Table> tables = tableDAO.getAllTables();
        assertNotNull(tables);
        assertFalse(tables.isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("Tìm kiếm bàn theo mã")
    void testSearchTables() {
        ArrayList<Table> tables = tableDAO.searchTables(TEST_TABLE_CODE);
        assertNotNull(tables);
        assertFalse(tables.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Lấy bàn theo mã")
    void testGetTableByCode() {
        Table table = tableDAO.getTableByCode(TEST_TABLE_CODE);
        assertNotNull(table);
        assertEquals(TEST_TABLE_CODE, table.getTableCode());
        assertEquals("Bàn Test DAO", table.getName());
        assertEquals(4, table.getCapacity());
    }

    @Test
    @Order(8)
    @DisplayName("Lấy bàn theo mã không tồn tại")
    void testGetTableByCodeNotFound() {
        Table table = tableDAO.getTableByCode("NONEXISTENT_TABLE_CODE");
        assertNull(table);
    }

    @Test
    @Order(9)
    @DisplayName("Kiểm tra mã bàn tồn tại")
    void testIsTableCodeExists() {
        assertTrue(tableDAO.isTableCodeExists(TEST_TABLE_CODE));
    }

    @Test
    @Order(10)
    @DisplayName("Kiểm tra mã bàn không tồn tại")
    void testIsTableCodeNotExists() {
        assertFalse(tableDAO.isTableCodeExists("NONEXISTENT_CODE"));
    }

    @Test
    @Order(11)
    @DisplayName("Kiểm tra mã bàn null")
    void testIsTableCodeExistsNull() {
        assertFalse(tableDAO.isTableCodeExists(null));
    }

    @Test
    @Order(12)
    @DisplayName("Cập nhật thông tin bàn")
    void testUpdateTable() {
        Table table = tableDAO.getTableByCode(TEST_TABLE_CODE);
        assertNotNull(table);
        table.setName("Bàn Test Cập Nhật");
        table.setCapacity(8);
        boolean result = tableDAO.updateTable(table);
        assertTrue(result);

        Table updated = tableDAO.getTableByCode(TEST_TABLE_CODE);
        assertNotNull(updated);
        assertEquals("Bàn Test Cập Nhật", updated.getName());
        assertEquals(8, updated.getCapacity());
    }

    @Test
    @Order(13)
    @DisplayName("Cập nhật bàn null")
    void testUpdateTableNull() {
        boolean result = tableDAO.updateTable(null);
        assertFalse(result);
    }

    @Test
    @Order(14)
    @DisplayName("Cập nhật bàn với ID không hợp lệ")
    void testUpdateTableInvalidId() {
        Table table = new Table();
        table.setId(0);
        table.setTableCode("X");
        table.setName("X");
        table.setCapacity(1);
        boolean result = tableDAO.updateTable(table);
        assertFalse(result);
    }

    @Test
    @Order(15)
    @DisplayName("Cập nhật trạng thái bàn")
    void testUpdateTableStatus() {
        boolean result = tableDAO.updateTableStatus(testTableId, Table.STATUS_SERVING);
        assertTrue(result);
    }

    @Test
    @Order(16)
    @DisplayName("Kiểm tra checkTableCode")
    void testCheckTableCode() {
        assertTrue(tableDAO.checkTableCode(TEST_TABLE_CODE));
        assertFalse(tableDAO.checkTableCode("NONEXISTENT"));
    }

    @Test
    @Order(17)
    @DisplayName("Tìm bàn trống")
    void testSearchFreeTable() {
        ArrayList<Table> tables = tableDAO.searchFreeTable("2099-01-01", "12:00", 4);
        assertNotNull(tables);
    }

    @Test
    @Order(18)
    @DisplayName("Kiểm tra bàn có giao dịch liên quan")
    void testHasTableRelatedBusiness() {
        boolean result = tableDAO.hasTableRelatedBusiness(testTableId);
        assertFalse(result);
    }

    @Test
    @Order(19)
    @DisplayName("Lấy danh sách bàn đang phục vụ")
    void testGetOccupiedTables() {
        ArrayList<Table> tables = tableDAO.getOccupiedTables();
        assertNotNull(tables);
    }

    @Test
    @Order(20)
    @DisplayName("Lấy danh sách bàn đang phục vụ (serving)")
    void testGetServingTables() {
        ArrayList<Table> tables = tableDAO.getServingTables();
        assertNotNull(tables);
    }

    @Test
    @Order(21)
    @DisplayName("Tìm kiếm bàn không tồn tại")
    void testSearchTablesNotFound() {
        ArrayList<Table> tables = tableDAO.searchTables("NONEXISTENT_TABLE_XYZ");
        assertNotNull(tables);
        assertTrue(tables.isEmpty());
    }

    @Test
    @Order(22)
    @DisplayName("Xóa bàn test")
    void testDeleteTable() {
        boolean result = tableDAO.deleteTable(testTableId);
        assertTrue(result);
    }

    @Test
    @Order(23)
    @DisplayName("Xóa bàn với ID không hợp lệ")
    void testDeleteTableInvalidId() {
        boolean result = tableDAO.deleteTable(0);
        assertFalse(result);
    }

    @Test
    @Order(24)
    @DisplayName("Xóa bàn với ID âm")
    void testDeleteTableNegativeId() {
        boolean result = tableDAO.deleteTable(-1);
        assertFalse(result);
    }
}
