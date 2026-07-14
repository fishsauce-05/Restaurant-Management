package dao.dish;

import model.Dish;
import java.util.ArrayList;

public interface IDishDAO {
    ArrayList<Dish> getAllDishes();

    ArrayList<Dish> searchDish(String keyword);

    ArrayList<Dish> searchDishes(String keyword);

    ArrayList<Dish> getDishesByCategory(String category);

    Dish getDishByCode(String dishCode);

    boolean addDish(Dish dish);

    boolean updateDish(Dish dish);

    boolean deleteDish(Dish dish);

    boolean deleteDish(int id);

    boolean deleteOrDeactivateDish(int id);

    boolean isDishCodeExists(String code);

    boolean hasDishBeenOrdered(int id);

    boolean checkDishCode(String code);
}
