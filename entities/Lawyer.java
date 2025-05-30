package entities;

public class Lawyer {
    private String nama;
    private String spesialisasi;
    private boolean tersedia;
    private double feePerCase;
    
    public Lawyer(String nama, String spesialisasi, double feePerCase) {
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.tersedia = true;
        this.feePerCase = feePerCase;
    }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getSpesialisasi() { return spesialisasi; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    
    public boolean isTersedia() { return tersedia; }
    public void setTersedia(boolean tersedia) { this.tersedia = tersedia; }

    public double getFeePerCase() { return feePerCase; }
    public void setFeePerCase(double feePerCase) { this.feePerCase = feePerCase; }
}