// IProcessPayment.java
package interfaces;
import entities.Payment;

// IProcessPayment.java
public interface IProcessPayment {
    Payment getPayment();
    boolean processPayment();
}