package dao.bill;

import model.*;
import java.sql.*;
import java.util.ArrayList;

import dao.DAO;

public class BillDAO extends DAO implements IBillDAO {

    public BillDAO() {
        super();
    }

    @Override
    public boolean createBill(Bill bill) {
        if (con == null) return false;
        boolean result = false;

        String sqlBill = "INSERT INTO tblBill(createTime, totalAmount, paymentMethod, tblOrderID, tblUserID) "
                + "VALUES(?,?,?,?,?)";
        String sqlUpdateOrder = "UPDATE tblOrder SET isPaid = 1, status = N'Đã thanh toán' WHERE id = ?";

        try {
            con.setAutoCommit(false);

            PreparedStatement ps1 = con.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS);
            ps1.setTimestamp(1, new Timestamp(bill.getCreatedTime().getTime()));
            ps1.setDouble(2, bill.getTotalAmount());
            ps1.setString(3, bill.getPaymentMethod());
            ps1.setInt(4, bill.getOrder().getId());
            ps1.setInt(5, bill.getUser().getId());
            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();
            if (rs.next()) {
                bill.setId(rs.getInt(1));
            }

            PreparedStatement ps2 = con.prepareStatement(sqlUpdateOrder);
            ps2.setInt(1, bill.getOrder().getId());
            ps2.executeUpdate();

            con.commit();
            result = true;
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Bill getBillById(int billId) {
        if (con == null) return null;

        String sql = "SELECT b.*, u.name AS staffName FROM tblBill b "
                + "JOIN tblUser u ON b.tblUserID = u.id WHERE b.id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Bill bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setCreatedTime(rs.getTimestamp("createTime"));
                bill.setTotalAmount(rs.getDouble("totalAmount"));
                bill.setPaymentMethod(rs.getString("paymentMethod"));

                User user = new User();
                user.setId(rs.getInt("tblUserID"));
                user.setName(rs.getString("staffName"));
                bill.setUser(user);

                Order order = new Order();
                order.setId(rs.getInt("tblOrderID"));
                bill.setOrder(order);
                return bill;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Bill> getBillsByDateRange(String startDate, String endDate) {
        ArrayList<Bill> list = new ArrayList<>();
        if (con == null) return list;

        String sql = "SELECT bl.id, CAST(bl.createTime AS DATE) AS paymentDate, CAST(bl.createTime AS TIME) AS paymentTime, bl.totalAmount, " +
                     "b.id AS bid, b.bookDate, b.bookTime, b.quantity, b.status " +
                     "FROM tblBill bl " +
                     "LEFT JOIN tblOrder o ON bl.tblOrderID = o.id " +
                     "LEFT JOIN tblBookedTable bt ON o.tblTableID = bt.tblTableID " +
                     "LEFT JOIN tblBooking b ON bt.tblBookingID = b.id " +
                     "WHERE CAST(bl.createTime AS DATE) BETWEEN ? AND ? ORDER BY bl.createTime ASC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapStatResultToBill(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Bill> getBillsByTimeFrm(String timeFrm, String startDate, String endDate) {
        ArrayList<Bill> list = new ArrayList<>();
        if (con == null) return list;

        String sql = "SELECT bl.id, CAST(bl.createTime AS DATE) AS paymentDate, CAST(bl.createTime AS TIME) AS paymentTime, bl.totalAmount, " +
                     "b.id AS bid, b.bookDate, b.bookTime, b.quantity, b.status " +
                     "FROM tblBill bl " +
                     "LEFT JOIN tblOrder o ON bl.tblOrderID = o.id " +
                     "LEFT JOIN tblBookedTable bt ON o.tblTableID = bt.tblTableID " +
                     "LEFT JOIN tblBooking b ON bt.tblBookingID = b.id " +
                     "WHERE CAST(bl.createTime AS DATE) BETWEEN ? AND ? ";

        if (timeFrm.equals("11:00-13:00")) {
            sql += "AND CAST(bl.createTime AS TIME) BETWEEN '11:00:00' AND '13:00:00' ";
        } else if (timeFrm.equals("18:00-20:00")) {
            sql += "AND CAST(bl.createTime AS TIME) BETWEEN '18:00:00' AND '20:00:00' ";
        } else {
            sql += "AND CAST(bl.createTime AS TIME) NOT BETWEEN '11:00:00' AND '13:00:00' " +
                   "AND CAST(bl.createTime AS TIME) NOT BETWEEN '18:00:00' AND '20:00:00' ";
        }
        sql += "ORDER BY bl.createTime ASC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapStatResultToBill(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Bill> getBillsByMonth(int month, int year) {
        ArrayList<Bill> list = new ArrayList<>();
        if (con == null) return list;

        String sql = "SELECT bl.id, CAST(bl.createTime AS DATE) AS paymentDate, CAST(bl.createTime AS TIME) AS paymentTime, bl.totalAmount, " +
                     "b.id AS bid, b.bookDate, b.bookTime, b.quantity, b.status " +
                     "FROM tblBill bl " +
                     "LEFT JOIN tblOrder o ON bl.tblOrderID = o.id " +
                     "LEFT JOIN tblBookedTable bt ON o.tblTableID = bt.tblTableID " +
                     "LEFT JOIN tblBooking b ON bt.tblBookingID = b.id " +
                     "WHERE MONTH(bl.createTime) = ? AND YEAR(bl.createTime) = ? " +
                     "ORDER BY bl.createTime ASC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapStatResultToBill(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Bill mapStatResultToBill(ResultSet rs) throws SQLException {
        Bill bl = new Bill();
        bl.setId(rs.getInt("id"));
        bl.setPaymentDate(rs.getDate("paymentDate"));
        bl.setPaymentTime(rs.getString("paymentTime"));
        bl.setTotalAmount(rs.getDouble("totalAmount"));

        int bookingId = rs.getInt("bid");
        if (!rs.wasNull()) {
            Booking b = new Booking();
            b.setId(bookingId);
            b.setBookDate(rs.getDate("bookDate"));
            b.setBookTime(rs.getString("bookTime"));
            b.setQuantity(rs.getInt("quantity"));
            b.setStatus(rs.getString("status"));
            bl.setBooking(b);
        }
        return bl;
    }
}
