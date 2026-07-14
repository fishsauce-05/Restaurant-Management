package dao.client;

import model.Client;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IClientDAO {
    ArrayList<Client> searchClient(String name, String phone);

    boolean addClient(Client c);

    List<Client> findAllActive() throws SQLException;

    List<Client> searchActive(String keyword) throws SQLException;

    Optional<Client> findById(int id) throws SQLException;

    String generateNextClientCode() throws SQLException;

    Client saveNew(Client client) throws SQLException;

    void saveExisting(Client client) throws SQLException;

    void update(Client client) throws SQLException;

    void softDelete(int id) throws SQLException;
}
