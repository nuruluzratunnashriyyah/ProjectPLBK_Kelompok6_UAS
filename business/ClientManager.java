package business;
import interfaces.IClientMgt;
import entities.Client;
import java.util.List;
import java.util.ArrayList;

public class ClientManager implements IClientMgt {
    private List<Client> clientsList;
    
    public ClientManager() {
        this.clientsList = new ArrayList<>();
    }
    
    @Override
    public Client getClient() {
        return clientsList.isEmpty() ? null : clientsList.get(0);
    }
    
    @Override
    public void getClientInfo() {
        if (clientsList.isEmpty()) {
            System.out.println("Belum ada client");
        } else {
            for (Client c : clientsList) {
                System.out.println("Nama: " + c.getNama());
                System.out.println("Email: " + c.getEmail());
                System.out.println("Kontak: " + c.getKontak());
                System.out.println("Alamat: " + c.getAlamat());
                System.out.println("--------------------");
            }
        }
    }
    
    @Override
    public void deleteClient() {
        System.out.println("Delete client implementation");
    }
    
    public boolean addClient(Client client) {
        if (getClientByEmail(client.getEmail()) != null) {
            return false; // Email already exists
        }
        clientsList.add(client);
        return true; // Successfully added
    }
    
    public Client getClientByEmail(String email) {
        for (Client c : clientsList) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }
    
    public List<Client> getAllClients() {
        return clientsList;
    }
    
    public void setClients(List<Client> clients) {
        this.clientsList = clients;
    }
}