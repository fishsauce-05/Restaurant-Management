package dao;

import dao.booking.BookingDAO;
import model.Booking;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingDAOTest {

    private BookingDAO bookingDAO;

    @BeforeAll
    void setUp() {
        bookingDAO = new BookingDAO();
    }

    @Test
    @Order(1)
    @DisplayName("Tìm kiếm đặt bàn với số điện thoại rỗng")
    void testSearchBookingEmptyPhone() {
        ArrayList<Booking> bookings = bookingDAO.searchBooking("");
        assertNotNull(bookings);
    }

    @Test
    @Order(2)
    @DisplayName("Tìm kiếm đặt bàn với số điện thoại không tồn tại")
    void testSearchBookingNotFound() {
        ArrayList<Booking> bookings = bookingDAO.searchBooking("0000000000");
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Lấy danh sách đặt bàn theo khoảng ngày")
    void testGetBookingsByDateRange() {
        ArrayList<Booking> bookings = bookingDAO.getBookingsByDateRange("2024-01-01", "2026-12-31");
        assertNotNull(bookings);
    }

    @Test
    @Order(4)
    @DisplayName("Lấy danh sách đặt bàn với khoảng ngày đảo ngược")
    void testGetBookingsByDateRangeReversed() {
        ArrayList<Booking> bookings = bookingDAO.getBookingsByDateRange("2026-12-31", "2024-01-01");
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Lấy danh sách đặt bàn với cùng ngày")
    void testGetBookingsByDateRangeSameDay() {
        ArrayList<Booking> bookings = bookingDAO.getBookingsByDateRange("2026-07-14", "2026-07-14");
        assertNotNull(bookings);
    }

    @Test
    @Order(6)
    @DisplayName("Tìm kiếm đặt bàn với ký tự đặc biệt")
    void testSearchBookingSpecialChars() {
        ArrayList<Booking> bookings = bookingDAO.searchBooking("!@#$%");
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Cập nhật đặt bàn với ID không tồn tại")
    void testUpdateBookingNotFound() {
        Booking booking = new Booking();
        booking.setId(-1);
        booking.setBookDate(new Date());
        booking.setBookTime("12:00");
        booking.setQuantity(4);
        boolean result = bookingDAO.updateBooking(booking);
        assertFalse(result);
    }

    @Test
    @Order(8)
    @DisplayName("Lấy đặt bàn theo ngày trong tương lai")
    void testGetBookingsByFutureDateRange() {
        ArrayList<Booking> bookings = bookingDAO.getBookingsByDateRange("2099-01-01", "2099-12-31");
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
    }
}
