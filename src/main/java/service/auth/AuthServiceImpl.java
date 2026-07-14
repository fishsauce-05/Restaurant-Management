package service.auth;

import model.User;
import java.sql.SQLException;
import java.util.Optional;
import dao.user.UserDAO;
import dao.user.IUserDAO;

public class AuthServiceImpl implements IAuthService {
    private final IUserDAO userDAO;

    public AuthServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthServiceImpl() {
        this(new UserDAO());
    }

    @Override
    public User login(String username, String password) throws SQLException {
        String normalizedUsername = validateUsername(username);
        validatePassword(password);

        Optional<User> authenticatedUser = userDAO.findActiveByUsernameAndPassword(normalizedUsername, password);
        return authenticatedUser.orElseThrow(() ->
                new IllegalArgumentException("Ten dang nhap hoac mat khau khong dung"));
    }

    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui long nhap ten dang nhap");
        }
        return username.trim();
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui long nhap mat khau");
        }
    }
}
