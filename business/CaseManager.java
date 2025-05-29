// CaseManager.java
package business;
import interfaces.ICaseMgt;
import entities.Cases;

// CaseManager.java
public class CaseManager implements ICaseMgt {
    private java.util.List<Cases> casesList;
    
    public CaseManager() {
        this.casesList = new java.util.ArrayList<>();
    }
    
    @Override
    public Cases getCase() {
        return casesList.isEmpty() ? null : casesList.get(0);
    }
    
    @Override
    public void getDetailCase() {
        if (casesList.isEmpty()) {
            System.out.println("Belum ada kasus");
        } else {
            for (Cases c : casesList) {
                System.out.println("Judul: " + c.getCaseTitle());
                System.out.println("Deskripsi: " + c.getCaseDescription());
                System.out.println("Status: " + c.getStatus());
                System.out.println("Pengacara: " + (c.getLawyerAssigned() != null ? c.getLawyerAssigned().getNama() : "Belum ditugaskan"));
                System.out.println("Status Pembayaran: " + (c.isPaid() ? "Sudah dibayar" : "Belum dibayar"));
                System.out.println("--------------------");
            }
        }
    }
    
    @Override
    public void assignLawyer() {
        // Implementation for assigning lawyer
    }
    
    public void addCase(Cases newCase) {
        casesList.add(newCase);
    }
    
    public java.util.List<Cases> getAllCases() {
        return casesList;
    }
    
    public Cases getCaseByTitle(String title) {
        for (Cases c : casesList) {
            if (c.getCaseTitle().equals(title)) {
                return c;
            }
        }
        return null;
    }
}