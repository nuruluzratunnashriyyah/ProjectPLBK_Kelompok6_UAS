package interfaces;
import entities.Payment;

public interface IProcessPayment {
    Payment getPayment();
    boolean processPayment();
}