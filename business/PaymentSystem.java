// PaymentSystem.java
package business;
import interfaces.IProcessPayment;
import entities.Payment;

// PaymentSystem.java
public class PaymentSystem implements IProcessPayment {
    private java.util.List<Payment> paymentsList;
    
    public PaymentSystem() {
        this.paymentsList = new java.util.ArrayList<>();
    }
    
    @Override
    public Payment getPayment() {
        return paymentsList.isEmpty() ? null : paymentsList.get(paymentsList.size() - 1);
    }
    
    @Override
    public boolean processPayment() {
        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            System.out.print("Masukkan nominal pembayaran: ");
            try {
                double nominal = scanner.nextDouble();
                if (nominal > 0) {
                    Payment payment = new Payment(nominal, "Berhasil", java.time.LocalDate.now().toString());
                    paymentsList.add(payment);
                    System.out.println("Pembayaran berhasil diproses!");
                    return true;
                } else {
                    System.out.println("Nominal tidak valid!");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Input tidak valid!");
                return false;
            }
        }
    }
    
    public boolean validatePayment(double expectedAmount, double paidAmount) {
        if (paidAmount == expectedAmount) {
            System.out.println("Pembayaran sesuai!");
            return true;
        } else if (paidAmount > expectedAmount) {
            double kembalian = paidAmount - expectedAmount;
            System.out.println("Pembayaran berlebih. Kembalian: Rp " + kembalian);
            return true;
        } else {
            double kekurangan = expectedAmount - paidAmount;
            System.out.println("Pembayaran kurang. Kekurangan: Rp " + kekurangan);
            return false;
        }
    }
}