package interfaces;
import entities.Lawyer;

public interface ILawyerMgt {
    Lawyer getLawyer();
    void addLawyer();
    void getLawyerLayerId();
    void deleteLawyer();
}