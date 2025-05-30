package entities;

public class LawyerHiring {
    private String caseStatus;
    private String hiringDate;
    private String clientEmail;
    private String lawyerName;
    
    public LawyerHiring(String caseStatus, String hiringDate, String lawyerEmail, String lawyerName) {
        this.caseStatus = caseStatus;
        this.hiringDate = hiringDate;
        this.clientEmail = clientEmail;
        this.lawyerName = lawyerName;
    }
    
    public String getCaseStatus() { return caseStatus; }
    public void setCaseStatus(String caseStatus) { this.caseStatus = caseStatus; }
    
    public String getHiringDate() { return hiringDate; }
    public void setHiringDate(String hiringDate) { this.hiringDate = hiringDate; }

    public String getClientEmail() { return clientEmail; }
    public String getLawyerName() { return lawyerName; }
}