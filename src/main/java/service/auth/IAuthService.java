package service.auth;

import model.User;
import java.sql.SQLException;

public interface IAuthService {
    User login(String username, String password) throws SQLException;
}
