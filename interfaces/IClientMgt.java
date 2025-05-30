package interfaces;
import entities.Client;

public interface IClientMgt {
    Client getClient();
    void getClientInfo();
    void deleteClient();
    Object getClientByEmail(String string);
}