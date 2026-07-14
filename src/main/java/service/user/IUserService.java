package service.user;

import model.User;
import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    List<User> getActiveUsers() throws SQLException;
    List<User> searchActiveUsers(String keyword) throws SQLException;
    User addUser(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    void softDeleteUser(int id, User currentUser) throws SQLException;
}
