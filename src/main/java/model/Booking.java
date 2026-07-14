package model;

import java.util.ArrayList;
import java.util.Date;

public class Booking extends BaseEntity {
    public static final String STATUS_PENDING = "Chờ nhận bàn";
    public static final String STATUS_CHECKED_IN = "Đã nhận bàn";
    public static final String STATUS_CANCELLED = "Đã hủy";

    private Date bookDate;
    private String bookTime;
    private int quantity;
    private Client client;
    private User user;
    private ArrayList<BookedTable> bookedTables;

    public Booking() {
        super();
        this.bookedTables = new ArrayList<BookedTable>();
    }

    public void addBookedTable(BookedTable bt) {
        this.bookedTables.add(bt);
    }

    public Date getBookDate() { return bookDate; }
    public void setBookDate(Date bookDate) { this.bookDate = bookDate; }

    public String getBookTime() { return bookTime; }
    public void setBookTime(String bookTime) { this.bookTime = bookTime; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public ArrayList<BookedTable> getBookedTables() { return bookedTables; }
    public void setBookedTables(ArrayList<BookedTable> bookedTables) { this.bookedTables = bookedTables; }
}
