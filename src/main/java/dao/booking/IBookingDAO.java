package dao.booking;

import model.Booking;

import java.util.ArrayList;

public interface IBookingDAO {
    boolean addBooking(Booking booking);

    boolean updateBooking(Booking booking);

    ArrayList<Booking> searchBooking(String phone);

    ArrayList<Booking> getBookingsByDateRange(String startDate, String endDate);
}
