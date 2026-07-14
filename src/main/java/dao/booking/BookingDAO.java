package dao.booking;
import model.*;
import java.sql.*;
import java.util.ArrayList;

import dao.DAO;

public class BookingDAO extends DAO {

    public BookingDAO() {
        super();
    }

    public boolean addBooking(Booking b) {
        if (con == null) {
            System.err.println("Lỗi: Kết nối CSDL chưa được khởi tạo!");
            return false;
        }
        boolean result = false;
        String sqlBooking = "INSERT INTO tblBooking(bookDate, bookTime, quantity, status, tblClientId, tblUserId) VALUES(?,?,?,?,?,?)";
        String sqlBookedTable = "INSERT INTO tblBookedTable(isCheckedIn, checkin, tblBookingId, tblTableId) VALUES(?,?,?,?)";
        String sqlUpdateTable = "UPDATE tblTable SET status = N'Đang phục vụ' WHERE id = ?";

        try {
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
            ps1.setDate(1, new java.sql.Date(b.getBookDate().getTime()));
            ps1.setString(2, b.getBookTime());
            ps1.setInt(3, b.getQuantity());
            ps1.setString(4, "Chờ nhận bàn");
            ps1.setInt(5, b.getClient().getId());
            ps1.setInt(6, b.getUser().getId());
            ps1.executeUpdate();

            ResultSet generatedKeys = ps1.getGeneratedKeys();
            if (generatedKeys.next()) {
                b.setId(generatedKeys.getInt(1));

                String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(b.getBookDate());
                String timeStr = b.getBookTime();
                java.sql.Timestamp checkInTimestamp;
                try {
                    java.util.Date parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr + " " + timeStr);
                    checkInTimestamp = new java.sql.Timestamp(parsedDate.getTime());
                } catch (Exception ex) {
                    checkInTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
                }

                PreparedStatement ps2 = con.prepareStatement(sqlBookedTable);
                PreparedStatement ps3 = con.prepareStatement(sqlUpdateTable);

                for (BookedTable bt : b.getBookedTables()) {
                    ps2.setInt(1, 1);
                    ps2.setTimestamp(2, checkInTimestamp);
                    ps2.setInt(3, b.getId());
                    ps2.setInt(4, bt.getTable().getId());
                    ps2.executeUpdate();

                    ps3.setInt(1, bt.getTable().getId());
                    ps3.executeUpdate();
                }
            }

            con.commit();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public ArrayList<Booking> searchBooking(String phone) {
        ArrayList<Booking> list = new ArrayList<>();

        String sql = "SELECT b.*, c.name, c.phone, c.email, c.address FROM tblBooking b "
                   + "JOIN tblClient c ON b.tblClientId = c.id "
                   + "WHERE c.phone LIKE ?";

        String sqlTable = "SELECT t.id, t.tableCode FROM tblBookedTable bt "
                        + "JOIN tblTable t ON bt.tblTableId = t.id "
                        + "WHERE bt.tblBookingId = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + phone + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setBookDate(rs.getDate("bookDate"));
                b.setBookTime(rs.getString("bookTime"));
                b.setQuantity(rs.getInt("quantity"));
                b.setStatus(rs.getString("status"));

                Client c = new Client();
                c.setId(rs.getInt("tblClientId"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                b.setClient(c);

                PreparedStatement psTable = con.prepareStatement(sqlTable);
                psTable.setInt(1, b.getId());
                ResultSet rsTable = psTable.executeQuery();

                while(rsTable.next()) {
                    Table t = new Table();
                    t.setId(rsTable.getInt("id"));
                    t.setTableCode(rsTable.getString("tableCode"));

                    BookedTable bt = new BookedTable();
                    bt.setTable(t);
                    b.addBookedTable(bt);
                }

                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateBooking(Booking b) {
        if (con == null) {
            System.err.println("Lỗi: Kết nối CSDL chưa được khởi tạo!");
            return false;
        }

        String sql = "UPDATE tblBooking SET bookDate = ?, bookTime = ?, quantity = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(b.getBookDate().getTime()));
            ps.setString(2, b.getBookTime());
            ps.setInt(3, b.getQuantity());
            ps.setInt(4, b.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Booking> getBookingsByDateRange(String startDate, String endDate) {
        ArrayList<Booking> list = new ArrayList<>();
        if (con == null) {
            System.err.println("Lỗi: Kết nối CSDL chưa được khởi tạo!");
            return list;
        }
        String sql = "SELECT b.id, b.bookDate, b.bookTime, b.quantity, b.status, " +
                      "c.id AS cid, c.name AS cname, c.phone AS cphone, " +
                      "u.id AS uid, u.name AS uname " +
                      "FROM tblBooking b " +
                      "JOIN tblClient c ON b.tblClientId = c.id " +
                      "LEFT JOIN tblUser u ON b.tblUserId = u.id " +
                      "WHERE b.bookDate BETWEEN ? AND ? ORDER BY b.bookDate ASC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setBookDate(rs.getDate("bookDate"));
                b.setBookTime(rs.getString("bookTime"));
                b.setQuantity(rs.getInt("quantity"));
                b.setStatus(rs.getString("status"));

                Client c = new Client();
                c.setId(rs.getInt("cid"));
                c.setName(rs.getString("cname"));
                c.setPhone(rs.getString("cphone"));
                b.setClient(c);

                User u = new User();
                u.setId(rs.getInt("uid"));
                u.setName(rs.getString("uname"));
                b.setUser(u);

                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
