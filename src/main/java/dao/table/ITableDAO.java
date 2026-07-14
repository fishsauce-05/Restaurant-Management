package dao.table;

import model.Table;
import java.util.ArrayList;

public interface ITableDAO {
    ArrayList<Table> searchFreeTable(String date, String time, int quantity);

    boolean checkTableAvailability(int tableId, String date, String time);

    ArrayList<Table> getOccupiedTables();

    ArrayList<Table> getServingTables();

    ArrayList<Table> getAllTables();

    boolean updateTableStatus(int tableId, String status);

    Table getTableByCode(String tableCode);

    ArrayList<Table> searchTables(String keyword);

    boolean addTable(Table table);

    boolean updateTable(Table table);

    boolean deleteTable(Table table);

    boolean deleteTable(int id);

    boolean deleteOrDeactivateTable(int id);

    boolean isTableCodeExists(String code);

    boolean hasTableRelatedBusiness(int id);

    boolean checkTableCode(String code);

    boolean checkoutTable(int tableId);
}
