package dao.bill;

import model.Bill;
import java.util.ArrayList;

public interface IBillDAO {

    boolean createBill(Bill bill);

    Bill getBillById(int billId);

    ArrayList<Bill> getBillsByDateRange(String startDate, String endDate);

    ArrayList<Bill> getBillsByTimeFrm(String timeFrm, String startDate, String endDate);

    ArrayList<Bill> getBillsByMonth(int month, int year);
}
