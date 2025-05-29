// Client.java
package entities;

// Client.java
public class Client {
    private String nama;
    private String kontak;
    private Cases cases;
    private String email;
    private String password;
    private String alamat;
    
    public Client(String nama, String kontak, String email, String password, String alamat) {
        this.nama = nama;
        this.kontak = kontak;
        this.email = email;
        this.password = password;
        this.alamat = alamat;
    }
    
    // Getters and Setters
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getKontak() { return kontak; }
    public void setKontak(String kontak) { this.kontak = kontak; }
    
    public Cases getCases() { return cases; }
    public void setCases(Cases cases) { this.cases = cases; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
}