package dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public abstract class DAO {
    public static Connection con;

    public DAO() {
        if (con == null) {
            String dbClass = "com.mysql.cj.jdbc.Driver";

            String username = resolveValue("restaurant.db.username", "RESTAURANT_DB_USERNAME", "root");
            String password = resolveValue("restaurant.db.password", "RESTAURANT_DB_PASSWORD", "123456");

            try {
                loadMySqlDriver(dbClass);
                con = connect(username, password);

                ensureManagerTables();

                System.out.println("Kết nối MySQL thành công!");
            } catch (ClassNotFoundException | SQLException e) {
                throw new IllegalStateException("Không thể kết nối MySQL cho restaurant_db. " + e.getMessage(), e);
            }
        }
    }

    private void loadMySqlDriver(String driverClassName) throws ClassNotFoundException, SQLException {
        try {
            Class.forName(driverClassName);
            return;
        } catch (ClassNotFoundException ignored) {
        }

        File jdbcJar = resolveJdbcJar();
        if (jdbcJar == null) {
            throw new ClassNotFoundException(driverClassName + " (Không tìm thấy JDBC jar trong classpath hoặc thư mục lib)");
        }

        try {
            URL jarUrl = jdbcJar.toURI().toURL();
            URLClassLoader loader = new URLClassLoader(new URL[] {jarUrl}, DAO.class.getClassLoader());
            Driver driver = (Driver) Class.forName(driverClassName, true, loader).getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new DriverShim(driver));
        } catch (ReflectiveOperationException | java.io.IOException exception) {
            throw new ClassNotFoundException(driverClassName + " (Không thể nạp JDBC jar từ " + jdbcJar.getAbsolutePath() + ")", exception);
        }
    }

    private File resolveJdbcJar() {

        String[] candidatePaths = new String[] {
                "lib\\mysql-connector-j-9.7.0.jar",
                "..\\target\\dependency\\mysql-connector-j-9.7.0.jar",
                "target\\dependency\\mysql-connector-j-9.7.0.jar"
        };

        for (String candidatePath : candidatePaths) {
            File file = new File(candidatePath);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    private Connection connect(String username, String password) throws SQLException {
        String overrideUrl = resolveValue("restaurant.db.url", "RESTAURANT_DB_URL", null);
        List<String> candidateUrls = new ArrayList<>();
        if (overrideUrl != null) {
            candidateUrls.add(overrideUrl);
        }

        String mysqlParams = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
        candidateUrls.add("jdbc:mysql://localhost:3306/restaurant_db" + mysqlParams);
        candidateUrls.add("jdbc:mysql://127.0.0.1:3306/restaurant_db" + mysqlParams);

        try {
            DriverManager.setLoginTimeout(2);
        } catch (Exception ignored) {
        }

        SQLException lastException = null;
        for (String candidateUrl : candidateUrls) {
            try {
                return DriverManager.getConnection(candidateUrl, username, password);
            } catch (SQLException exception) {
                lastException = exception;
            }
        }

        throw new SQLException(buildConnectionError(candidateUrls, username, lastException), lastException);
    }

    private String resolveValue(String systemPropertyName, String environmentVariableName, String defaultValue) {
        String systemValue = System.getProperty(systemPropertyName);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String environmentValue = System.getenv(environmentVariableName);
        if (environmentValue != null && !environmentValue.trim().isEmpty()) {
            return environmentValue.trim();
        }

        return defaultValue;
    }

    private String buildConnectionError(List<String> candidateUrls, String username, SQLException lastException) {
        return "Đã thử các JDBC URL " + candidateUrls
                + " với username '" + username + "' nhưng đều thất bại. "
                + "Kiểm tra MySQL có đang chạy, database restaurant_db đã tồn tại chưa "
                + "và mật khẩu/user đúng. "
                + "Lỗi cuối cùng: " + (lastException == null ? "unknown" : lastException.getMessage());
    }

    private static final class DriverShim implements Driver {
        private final Driver driver;

        private DriverShim(Driver driver) {
            this.driver = driver;
        }

        @Override
        public Connection connect(String url, java.util.Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public java.sql.DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }

    private boolean isColumnExists(String tableName, String columnName) throws SQLException {
        String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                       "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean isTableExists(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES " +
                       "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void ensureManagerTables() throws SQLException {
        try (Statement statement = con.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS tblUser ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "userCode VARCHAR(20) NULL UNIQUE, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "role VARCHAR(20) NOT NULL, "
                    + "phone VARCHAR(20) NULL, "
                    + "email VARCHAR(100) NULL, "
                    + "status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'"
                    + ")");

            if (isTableExists("tblClient") && !isColumnExists("tblClient", "status")) {
                statement.execute("ALTER TABLE tblClient ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'");
            }

            if (isTableExists("tblUser") && !isColumnExists("tblUser", "status")) {
                statement.execute("ALTER TABLE tblUser ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'");
            }

            if (isTableExists("tblUser") && !isColumnExists("tblUser", "userCode")) {
                statement.execute("ALTER TABLE tblUser ADD COLUMN userCode VARCHAR(20) NULL");
            }
        }

        seedDefaultUsers();
    }

    private void seedDefaultUsers() throws SQLException {
        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM tblUser")) {
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return;
            }
        }

        String sql = "INSERT INTO tblUser(userCode, username, password, name, role, phone, email, status) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            addUserSeed(statement, "NV001", "admin", "123456", "System Manager", "MANAGER", "0911000001", "admin@restaurant.local");
            addUserSeed(statement, "NV002", "staff01", "123456", "Floor Staff One", "STAFF", "0911000002", "staff01@restaurant.local");
            statement.executeBatch();
        }
    }

    private void addUserSeed(PreparedStatement statement, String userCode, String username, String password,
                             String name, String role, String phone, String email) throws SQLException {
        statement.setString(1, userCode);
        statement.setString(2, username);
        statement.setString(3, password);
        statement.setString(4, name);
        statement.setString(5, role);
        statement.setString(6, phone);
        statement.setString(7, email);
        statement.setString(8, "ACTIVE");
        statement.addBatch();
    }
}
