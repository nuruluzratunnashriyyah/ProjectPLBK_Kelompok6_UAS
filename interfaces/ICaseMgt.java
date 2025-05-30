package interfaces;
import entities.Cases;

public interface ICaseMgt {
    Cases getCase();
    void getDetailCase();
    void assignLawyer();
}