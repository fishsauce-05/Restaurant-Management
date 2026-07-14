package dao.bill;

import model.Bill;

public interface IBillDAO {

    boolean createBill(Bill bill);

    Bill getBillById(int billId);
}
