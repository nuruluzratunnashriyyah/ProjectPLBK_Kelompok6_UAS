// ClientManager.java
package business;
import interfaces.IClientMgt;
import entities.Client;

// ClientManager.java
public class ClientManager implements IClientMgt {
    private java.util.List<Client> clientsList;
    
    public ClientManager() {
        this.clientsList = new java.util.ArrayList<>();
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
        // Implementation for deleting client
    }
    
    public void addClient(Client client) {
        clientsList.add(client);
    }
    
    public Client getClientByEmail(String email) {
        for (Client c : clientsList) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }
    
    public java.util.List<Client> getAllClients() {
        return clientsList;
    }
}