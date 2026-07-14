package dao.dish;

import model.*;
import java.sql.*;
import java.util.ArrayList;

import dao.DAO;

public class DishStatDAO extends DAO implements IDishStatDAO {

    public DishStatDAO() {
        super();
    }

    @Override
    public ArrayList<DishStat> getBestSellingDish(String startDate, String endDate) {
        ArrayList<DishStat> list = new ArrayList<>();
        if (con == null) return list;
        String sql = "SELECT d.id, d.dishCode, d.name, d.category, d.price, "
                + "SUM(oi.quantity) AS totalQuantity, "
                + "SUM(oi.quantity * oi.currentPrice) AS totalRevenue "
                + "FROM tblDish d "
                + "JOIN tblOrderDish oi ON d.id = oi.tblDishID "
                + "JOIN tblOrder o ON oi.tblOrderID = o.id "
                + "JOIN tblBill b ON o.id = b.tblOrderID "
                + "WHERE b.createTime >= ? AND b.createTime <= ? "
                + "GROUP BY d.id, d.dishCode, d.name, d.category, d.price "
                + "ORDER BY totalRevenue DESC";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate + " 00:00:00");
            ps.setString(2, endDate + " 23:59:59");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DishStat ds = new DishStat();
                ds.setId(rs.getInt("id"));
                ds.setDishCode(rs.getString("dishCode"));
                ds.setName(rs.getString("name"));
                ds.setCategory(rs.getString("category"));
                ds.setPrice(rs.getInt("price"));
                ds.setTotalQuantity(rs.getInt("totalQuantity"));
                ds.setTotalRevenue(rs.getInt("totalRevenue"));
                list.add(ds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int getTotalRevenue(String startDate, String endDate) {
        if (con == null) return 0;
        String sql = "SELECT SUM(totalAmount) AS totalRevenue FROM tblBill "
                + "WHERE createTime >= ? AND createTime <= ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, startDate + " 00:00:00");
            ps.setString(2, endDate + " 23:59:59");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalRevenue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<DishStat> getTopSellingDish(String startDate, String endDate, int topN) {
        ArrayList<DishStat> allList = getBestSellingDish(startDate, endDate);
        ArrayList<DishStat> topList = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, allList.size()); i++) {
            topList.add(allList.get(i));
        }
        return topList;
    }
}
