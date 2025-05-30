package business;
import interfaces.ICaseMgt;
import entities.Cases;
import entities.Lawyer;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CaseManager implements ICaseMgt {
    private List<Cases> casesList;
    
    public CaseManager() {
        this.casesList = new ArrayList<>();
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
        System.out.println("Assign lawyer implementation");
    }
    
    public void addCase(Cases newCase) {
        casesList.add(newCase);
    }
    
    public List<Cases> getAllCases() {
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
    
    public Cases getClientCase(String clientEmail) {
        for (Cases c : casesList) {
            if (c.getClient() != null && c.getClient().getEmail().equals(clientEmail)) {
                return c;
            }
        }
        return null;
    }
    
    public void setCases(List<Cases> cases) {
        this.casesList = cases;
    }

    public void assignLawyerToCase(Cases caseToAssign, Lawyer lawyerToAssign) {
        caseToAssign.setLawyerAssigned(lawyerToAssign);
        caseToAssign.setStatus("Assigned");
        caseToAssign.setFee(lawyerToAssign.getFeePerCase());
        lawyerToAssign.setTersedia(false);
    }
}