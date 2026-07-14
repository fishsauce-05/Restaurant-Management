package dao.dish;

import model.DishStat;
import java.util.ArrayList;

public interface IDishStatDAO {
    ArrayList<DishStat> getBestSellingDish(String startDate, String endDate);

    int getTotalRevenue(String startDate, String endDate);

    ArrayList<DishStat> getTopSellingDish(String startDate, String endDate, int topN);
}
