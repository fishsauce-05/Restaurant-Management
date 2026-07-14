package dao;

import dao.client.ClientDAO;
import model.Client;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientDAOTest {

    private ClientDAO clientDAO;
    private int testClientId;
    private final String TEST_PHONE = "0999888777";
    private final String TEST_NAME = "Test Client DAO";

    @BeforeAll
    void setUp() {
        clientDAO = new ClientDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Thêm khách hàng mới")
    void testAddClient() {
        Client client = new Client();
        client.setName(TEST_NAME);
        client.setPhone(TEST_PHONE);
        client.setEmail("testclient@test.com");
        client.setAddress("123 Test Street");
        boolean result = clientDAO.addClient(client);
        assertTrue(result);
        assertTrue(client.getId() > 0);
        testClientId = client.getId();
    }

    @Test
    @Order(2)
    @DisplayName("Tìm khách hàng theo ID")
    void testFindById() throws Exception {
        Optional<Client> client = clientDAO.findById(testClientId);
        assertTrue(client.isPresent());
        assertEquals(TEST_NAME, client.get().getName());
        assertEquals(TEST_PHONE, client.get().getPhone());
    }

    @Test
    @Order(3)
    @DisplayName("Tìm khách hàng theo ID không tồn tại")
    void testFindByIdNotFound() throws Exception {
        Optional<Client> client = clientDAO.findById(-1);
        assertFalse(client.isPresent());
    }

    @Test
    @Order(4)
    @DisplayName("Tìm kiếm khách hàng theo tên")
    void testSearchClientByName() {
        ArrayList<Client> clients = clientDAO.searchClient("Test Client", null);
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Tìm kiếm khách hàng theo số điện thoại")
    void testSearchClientByPhone() {
        ArrayList<Client> clients = clientDAO.searchClient(null, TEST_PHONE);
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("Tìm kiếm khách hàng theo cả tên và số điện thoại")
    void testSearchClientByNameAndPhone() {
        ArrayList<Client> clients = clientDAO.searchClient(TEST_NAME, TEST_PHONE);
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Lấy danh sách khách hàng đang hoạt động")
    void testFindAllActive() throws Exception {
        List<Client> clients = clientDAO.findAllActive();
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("Tìm kiếm khách hàng hoạt động theo từ khóa")
    void testSearchActive() throws Exception {
        List<Client> clients = clientDAO.searchActive("Test");
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
    }

    @Test
    @Order(9)
    @DisplayName("Tìm kiếm khách hàng hoạt động với từ khóa rỗng")
    void testSearchActiveEmptyKeyword() throws Exception {
        List<Client> clients = clientDAO.searchActive("");
        assertNotNull(clients);
    }

    @Test
    @Order(10)
    @DisplayName("Tìm kiếm khách hàng hoạt động với từ khóa null")
    void testSearchActiveNull() throws Exception {
        List<Client> clients = clientDAO.searchActive(null);
        assertNotNull(clients);
    }

    @Test
    @Order(11)
    @DisplayName("Cập nhật thông tin khách hàng")
    void testUpdateClient() throws Exception {
        Client client = clientDAO.findById(testClientId).orElse(null);
        assertNotNull(client);
        client.setName("Updated Test Client");
        client.setAddress("456 Updated Street");
        clientDAO.update(client);

        Client updated = clientDAO.findById(testClientId).orElse(null);
        assertNotNull(updated);
        assertEquals("Updated Test Client", updated.getName());
        assertEquals("456 Updated Street", updated.getAddress());
    }

    @Test
    @Order(12)
    @DisplayName("Tìm kiếm khách hàng không tồn tại")
    void testSearchClientNotFound() {
        ArrayList<Client> clients = clientDAO.searchClient("NONEXISTENT_CLIENT_XYZ", null);
        assertNotNull(clients);
        assertTrue(clients.isEmpty());
    }

    @Test
    @Order(13)
    @DisplayName("Tạo mã khách hàng tiếp theo")
    void testGenerateNextClientCode() throws Exception {
        String code = clientDAO.generateNextClientCode();
        assertNull(code);
    }

    @Test
    @Order(14)
    @DisplayName("Xóa mềm khách hàng")
    void testSoftDelete() throws Exception {
        clientDAO.softDelete(testClientId);
        Optional<Client> client = clientDAO.findById(testClientId);
        assertTrue(client.isPresent());
        assertEquals("INACTIVE", client.get().getStatus());
    }

    @AfterAll
    void tearDown() {
        try {
            java.sql.Connection con = dao.DAO.con;
            if (con != null) {
                java.sql.PreparedStatement ps = con.prepareStatement("DELETE FROM tblClient WHERE id = ?");
                ps.setInt(1, testClientId);
                ps.executeUpdate();
            }
        } catch (Exception ignored) {
        }
    }
}
