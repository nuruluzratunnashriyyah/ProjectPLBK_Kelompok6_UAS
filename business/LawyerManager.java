// LawyerManager.java
package business;
import interfaces.ILawyerMgt;
import entities.Lawyer;

public class LawyerManager implements ILawyerMgt {
    private java.util.List<Lawyer> lawyersList;
    
    public LawyerManager() {
        this.lawyersList = new java.util.ArrayList<>();
        // Add default lawyers
        lawyersList.add(new Lawyer("Andi Wijaya", "Pidana"));
        lawyersList.add(new Lawyer("Sari Indah", "Perdata"));
        lawyersList.add(new Lawyer("Budi Santoso", "Bisnis"));
    }
    
    @Override
    public Lawyer getLawyer() {
        return lawyersList.isEmpty() ? null : lawyersList.get(0);
    }
    
    @Override
    public void addLawyer() {
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            System.out.print("Nama Pengacara: ");
            String nama = scanner.nextLine();
            System.out.print("Spesialisasi: ");
            String spesialisasi = scanner.nextLine();
            
            Lawyer newLawyer = new Lawyer(nama, spesialisasi);
            lawyersList.add(newLawyer);
        }
        System.out.println("Pengacara berhasil ditambahkan!");
    }
    
    @Override
    public void getLawyerLayerId() {
        if (lawyersList.isEmpty()) {
            System.out.println("Tidak ditemukan pengacara dengan spesialisasi tersebut");
        } else {
            System.out.println("Daftar Pengacara:");
            for (int i = 0; i < lawyersList.size(); i++) {
                Lawyer l = lawyersList.get(i);
                System.out.println((i+1) + ". " + l.getNama() + " - " + l.getSpesialisasi() + 
                    " (" + (l.isTersedia() ? "Tersedia" : "Tidak Tersedia") + ")");
            }
        }
    }
    
    @Override
    public void deleteLawyer() {
        // Implementation for deleting lawyer
    }
    
    public java.util.List<Lawyer> getAvailableLawyers() {
        java.util.List<Lawyer> available = new java.util.ArrayList<>();
        for (Lawyer l : lawyersList) {
            if (l.isTersedia()) {
                available.add(l);
            }
        }
        return available;
    }
    
    public Lawyer getLawyerByName(String name) {
        for (Lawyer l : lawyersList) {
            if (l.getNama().equals(name)) {
                return l;
            }
        }
        return null;
    }
}