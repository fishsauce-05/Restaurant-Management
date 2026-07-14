package dao.client;

import model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DAO;

public class ClientDAO extends DAO implements IClientDAO {
    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final String INACTIVE_STATUS = "INACTIVE";

    public ClientDAO() {
        super();
    }

    @Override
    public ArrayList<Client> searchClient(String name, String phone) {
        ArrayList<Client> list = new ArrayList<>();

        String sql = "SELECT * FROM tblClient WHERE 1=1";

        if (name != null && !name.isEmpty()) {
            sql += " AND name LIKE ?";
        }
        if (phone != null && !phone.isEmpty()) {
            sql += " AND phone LIKE ?";
        }

        try {
            PreparedStatement ps = con.prepareStatement(sql);

            int paramIndex = 1;
            if (name != null && !name.isEmpty()) {
                ps.setString(paramIndex++, "%" + name + "%");
            }
            if (phone != null && !phone.isEmpty()) {
                ps.setString(paramIndex++, "%" + phone + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("ID"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean addClient(Client c) {
        String sql = "INSERT INTO tblClient(name, phone, email, address, status) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setString(5, ACTIVE_STATUS);
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.setId(generatedKeys.getInt(1));
                }
            }
            c.setStatus(ACTIVE_STATUS);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Client> findAllActive() throws SQLException {
        String sql = "SELECT id, name, phone, email, address, status FROM tblClient "
                + "WHERE status IS NULL OR status = ? ORDER BY name, id";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, ACTIVE_STATUS);
            return readClients(statement);
        }
    }

    @Override
    public List<Client> searchActive(String keyword) throws SQLException {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        if (normalizedKeyword.isEmpty()) {
            return findAllActive();
        }

        String sql = "SELECT id, name, phone, email, address, status FROM tblClient "
                + "WHERE (status IS NULL OR status = ?) AND (name LIKE ? OR phone LIKE ? OR email LIKE ? OR address LIKE ?) "
                + "ORDER BY name, id";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            String likeKeyword = "%" + normalizedKeyword + "%";
            statement.setString(1, ACTIVE_STATUS);
            statement.setString(2, likeKeyword);
            statement.setString(3, likeKeyword);
            statement.setString(4, likeKeyword);
            statement.setString(5, likeKeyword);
            return readClients(statement);
        }
    }

    @Override
    public Optional<Client> findById(int id) throws SQLException {
        String sql = "SELECT id, name, phone, email, address, status FROM tblClient WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapClient(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public String generateNextClientCode() throws SQLException {
        return null;
    }

    @Override
    public Client saveNew(Client client) throws SQLException {
        if (!addClient(client)) {
            throw new SQLException("Could not insert client into tblClient");
        }
        return client;
    }

    @Override
    public void saveExisting(Client client) throws SQLException {
        update(client);
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE tblClient SET name = ?, phone = ?, email = ?, address = ? WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getPhone());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getAddress());
            statement.setInt(5, client.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void softDelete(int id) throws SQLException {
        String sql = "UPDATE tblClient SET status = ? WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, INACTIVE_STATUS);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    private List<Client> readClients(PreparedStatement statement) throws SQLException {
        List<Client> clients = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clients.add(mapClient(resultSet));
            }
        }
        return clients;
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setStatus(rs.getString("status"));
        c.setClientCode("KH" + String.format("%04d", c.getId()));
        return c;
    }
}
