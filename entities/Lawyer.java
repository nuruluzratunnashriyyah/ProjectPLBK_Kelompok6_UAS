// Lawyer.java
package entities;

// Lawyer.java
public class Lawyer {
    private String nama;
    private String spesialisasi;
    private boolean tersedia;
    
    public Lawyer(String nama, String spesialisasi) {
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.tersedia = true;
    }
    
    // Getters and Setters
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getSpesialisasi() { return spesialisasi; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    
    public boolean isTersedia() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }
}