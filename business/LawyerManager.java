package business;
import interfaces.ILawyerMgt;
import entities.Lawyer;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import database.DatabaseManager;

public class LawyerManager implements ILawyerMgt {
    private List<Lawyer> lawyersList;
    private Scanner scanner;
    
    public LawyerManager() {
        this.lawyersList = new ArrayList<>();
    }
    
    @Override
    public Lawyer getLawyer() {
        return lawyersList.isEmpty() ? null : lawyersList.get(0);
    }
    
    @Override
    public void addLawyer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nama Pengacara: ");
        String nama = scanner.nextLine();
        System.out.print("Spesialisasi: ");
        String spesialisasi = scanner.nextLine();
        System.out.print("Fee per Kasus: ");
        double fee = scanner.nextDouble();
        scanner.nextLine();
        
        Lawyer newLawyer = new Lawyer(nama, spesialisasi, fee);
        lawyersList.add(newLawyer);
        System.out.println("Pengacara berhasil ditambahkan!");
    }
    
    @Override
    public void getLawyerLayerId() {
        if (lawyersList.isEmpty()) {
            System.out.println("Tidak ada pengacara tersedia");
        } else {
            System.out.println("Daftar Pengacara:");
            for (int i = 0; i < lawyersList.size(); i++) {
                Lawyer l = lawyersList.get(i);
                System.out.printf("%d. %s - %s (Fee: Rp %,.2f) (%s)\n",
                    (i+1), l.getNama(), l.getSpesialisasi(), 
                    l.getFeePerCase(), l.isTersedia() ? "Tersedia" : "Tidak Tersedia");
            }
        }
    }
    
    @Override
    public void deleteLawyer() {
        List<Lawyer> lawyers = this.getAllLawyers();
        if (lawyers.isEmpty()) {
            System.out.println("Tidak ada pengacara yang tersedia.");
            return;
        }

        System.out.println("\nDaftar Pengacara:");
        for (int i = 0; i < lawyers.size(); i++) {
            Lawyer l = lawyers.get(i);
            System.out.printf("%d. %s - %s\n", (i+1), l.getNama(), l.getSpesialisasi());
        }

        System.out.print("Pilih nomor pengacara yang akan dihapus (0 untuk batal): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice == 0) return;
            
            if (choice < 1 || choice > lawyers.size()) {
                System.out.println("Nomor tidak valid!");
                return;
            }
            
            Lawyer toDelete = lawyers.get(choice-1);
            
            // Check if lawyer is assigned to any active cases
            if (!toDelete.isTersedia()) {
                System.out.println("Pengacara tidak dapat dihapus karena sedang menangani kasus!");
                return;
            }
            
            lawyers.remove(choice-1);
            DatabaseManager.saveLawyers(lawyers);
            System.out.println("Pengacara berhasil dihapus!");
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
            scanner.nextLine();
        }
    }
    
    public List<Lawyer> getAvailableLawyers() {
        List<Lawyer> available = new ArrayList<>();
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
    
    public List<Lawyer> getAllLawyers() {
        return lawyersList;
    }
    
    public void setLawyers(List<Lawyer> lawyers) {
        this.lawyersList = lawyers;
    }
}