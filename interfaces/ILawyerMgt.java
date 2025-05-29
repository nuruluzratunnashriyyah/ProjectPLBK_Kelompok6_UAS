// ILawyerMgt.java
package interfaces;
import entities.Lawyer;

// ILawyerMgt.java
public interface ILawyerMgt {
    Lawyer getLawyer();
    void addLawyer();
    void getLawyerLayerId();
    void deleteLawyer();
}