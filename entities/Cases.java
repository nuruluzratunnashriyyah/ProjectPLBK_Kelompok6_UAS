package entities;

public class Cases {
    private String caseTitle;
    private String caseDescription;
    private Lawyer lawyerAssigned;
    private String status;
    private double fee;
    private boolean isPaid;
    private Client client;
    
    public Cases(String caseTitle, String caseDescription) {
        this.caseTitle = caseTitle;
        this.caseDescription = caseDescription;
        this.status = "Pending";
        this.isPaid = false;
    }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public String getCaseTitle() { return caseTitle; }
    public void setCaseTitle(String caseTitle) { this.caseTitle = caseTitle; }
    
    public String getCaseDescription() { return caseDescription; }
    public void setCaseDescription(String caseDescription) { this.caseDescription = caseDescription; }
    
    public Lawyer getLawyerAssigned() { return lawyerAssigned; }
    public void setLawyerAssigned(Lawyer lawyerAssigned) { this.lawyerAssigned = lawyerAssigned; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
    
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}