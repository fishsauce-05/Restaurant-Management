package model;

import java.util.Date;

public class BookedTable extends BaseEntity {
    private Date checkIn;
    private Date checkOut;
    private double price;
    private boolean isCheckedIn;
    private Table table;

    public BookedTable() {
        super();
    }

    public Date getCheckIn() { return checkIn; }
    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isCheckedIn() { return isCheckedIn; }
    public void setCheckedIn(boolean isCheckedIn) { this.isCheckedIn = isCheckedIn; }

    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }
}
