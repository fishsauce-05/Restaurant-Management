package service.client;

import model.Client;
import java.sql.SQLException;
import java.util.List;

public interface IClientService {
    List<Client> getActiveClients() throws SQLException;
    List<Client> searchActiveClients(String keyword) throws SQLException;
    Client addClient(Client client) throws SQLException;
    void updateClient(Client client) throws SQLException;
    void softDeleteClient(int id) throws SQLException;
}
