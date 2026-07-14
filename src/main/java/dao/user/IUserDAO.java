package dao.user;

import model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    Optional<User> findActiveByUsernameAndPassword(String username, String password) throws SQLException;

    List<User> findAllActive() throws SQLException;

    List<User> searchActive(String keyword) throws SQLException;

    Optional<User> findById(int id) throws SQLException;

    boolean existsByUsername(String username, Integer excludingId) throws SQLException;

    boolean existsByPhone(String phone, Integer excludingId) throws SQLException;

    boolean existsByEmail(String email, Integer excludingId) throws SQLException;

    int countActiveManagers() throws SQLException;

    String generateNextUserCode() throws SQLException;

    User addUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    void softDeleteUser(int id) throws SQLException;
}
