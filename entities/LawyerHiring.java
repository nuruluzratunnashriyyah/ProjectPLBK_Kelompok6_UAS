package entities;

public class LawyerHiring {
    private String caseStatus;
    private String hiringDate;
    
    public LawyerHiring(String caseStatus, String hiringDate) {
        this.caseStatus = caseStatus;
        this.hiringDate = hiringDate;
    }
    
    public String getCaseStatus() { return caseStatus; }
    public void setCaseStatus(String caseStatus) { this.caseStatus = caseStatus; }
    
    public String getHiringDate() { return hiringDate; }
    public void setHiringDate(String hiringDate) { this.hiringDate = hiringDate; }
}