// LawyerHiringSystem.java
package business;
import interfaces.ILawyerMgt;
import entities.Lawyer;
import entities.Cases;
import interfaces.ICancelHire;
import interfaces.ICaseMgt;
import interfaces.IClientMgt;
import interfaces.IMakeHire;
import interfaces.IMakePayment;
import interfaces.IProcessPayment;
import entities.Payment;
import entities.Client;

public class LawyerHiringSystem implements IMakeHire, ICancelHire, IMakePayment {
    private IClientMgt clientManager;
    private ILawyerMgt lawyerManager;
    private IProcessPayment paymentSystem;
    private ICaseMgt caseManager;
    
    private Client currentUser;
    private boolean isAdmin;
    private java.util.Scanner scanner;
    
    public LawyerHiringSystem() {
        this.clientManager = new ClientManager();
        this.lawyerManager = new LawyerManager();
        this.paymentSystem = new PaymentSystem();
        this.caseManager = new CaseManager();
        this.scanner = new java.util.Scanner(System.in);
        this.isAdmin = false;
        
        // Add default admin
        ((ClientManager)clientManager).addClient(new Client("Admin", "admin@system.com", "admin@system.com", "admin123", "System"));
    }
    
    public void start() {
        System.out.println("=== SISTEM HIRING PENGACARA ===");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                if (isAdmin) {
                    showAdminMenu();
                } else {
                    showClientMenu();
                }
            }
        }
    }
    
    private void showLoginMenu() {
        System.out.println("\n1. Daftar");
        System.out.println("2. Masuk");
        System.out.println("3. Keluar");
        System.out.print("Pilih menu: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                System.out.println("Terima kasih!");
                System.exit(0);
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }
    
    private void register() {
        System.out.println("\n=== DAFTAR PENGGUNA ===");
        System.out.print("Nama: ");
        String nama = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Kata Sandi: ");
        String password = scanner.nextLine();
        
        System.out.print("Konfirmasi Kata Sandi: ");
        String confirmPassword = scanner.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Konfirmasi kata sandi tidak cocok!");
            return;
        }
        
        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();
        
        // Check if email already exists
        if (((ClientManager)clientManager).getClientByEmail(email) != null) {
            System.out.println("Email sudah terdaftar!");
            return;
        }
        
        Client newClient = new Client(nama, email, email, password, alamat);
        ((ClientManager)clientManager).addClient(newClient);
        System.out.println("Pendaftaran berhasil!");
    }
    
    private void login() {
        System.out.println("\n=== MASUK ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Kata Sandi: ");
        String password = scanner.nextLine();
        
        Client client = ((ClientManager)clientManager).getClientByEmail(email);
        
        if (client != null && client.getPassword().equals(password)) {
            currentUser = client;
            if (email.equals("admin@system.com")) {
                isAdmin = true;
                System.out.println("Selamat datang, Admin!");
            } else {
                isAdmin = false;
                System.out.println("Selamat datang, " + client.getNama() + "!");
            }
        } else {
            System.out.println("Email atau kata sandi salah!");
        }
    }
    
    private void showAdminMenu() {
        System.out.println("\n=== MENU ADMIN ===");
        System.out.println("1. Manage Cases");
        System.out.println("2. Manage Client");
        System.out.println("3. Manage Lawyer");
        System.out.println("4. Memeriksa Pembayaran");
        System.out.println("5. Keluar");
        System.out.print("Pilih menu: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                manageCases();
                break;
            case 2:
                manageClient();
                break;
            case 3:
                manageLawyer();
                break;
            case 4:
                checkPayment();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }
    
    private void showClientMenu() {
        System.out.println("\n=== MENU CLIENT ===");
        System.out.println("1. Add Case");
        System.out.println("2. Membatalkan Kasus");
        System.out.println("3. Melihat Daftar Pengacara");
        System.out.println("4. Melakukan Pembayaran");
        System.out.println("5. Keluar");
        System.out.print("Pilih menu: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addCase();
                break;
            case 2:
                cancelCase();
                break;
            case 3:
                lawyerManager.getLawyerLayerId();
                break;
            case 4:
                makePayment();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Pilihan tidak valid!");
        }
    }
    
    @Override
    public void addCase() {
        System.out.println("\n=== TAMBAH KASUS ===");
        System.out.print("Judul Kasus: ");
        String title = scanner.nextLine();
        
        System.out.print("Deskripsi Kasus: ");
        String description = scanner.nextLine();
        
        Cases newCase = new Cases(title, description);
        newCase.setFee(500000); // Set default fee
        
        ((CaseManager)caseManager).addCase(newCase);
        currentUser.setCases(newCase);
        
        System.out.println("Kasus berhasil ditambahkan!");
        System.out.println("Biaya pengacara: Rp " + newCase.getFee());
        System.out.println("Status: " + newCase.getStatus());
    }
    
    @Override
    public void cancelCase() {
        if (currentUser.getCases() == null) {
            System.out.println("Anda belum memiliki kasus yang dapat dibatalkan.");
            return;
        }
        
        System.out.println("Apakah Anda yakin ingin membatalkan kasus? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            currentUser.setCases(null);
            System.out.println("Kasus berhasil dibatalkan.");
        } else {
            System.out.println("Pembatalan kasus dibatalkan.");
        }
    }
    
    @Override
    public void getCase() {
        if (currentUser.getCases() != null) {
            Cases userCase = currentUser.getCases();
            System.out.println("Kasus Anda:");
            System.out.println("Judul: " + userCase.getCaseTitle());
            System.out.println("Deskripsi: " + userCase.getCaseDescription());
            System.out.println("Status: " + userCase.getStatus());
            System.out.println("Biaya: Rp " + userCase.getFee());
            System.out.println("Status Pembayaran: " + (userCase.isPaid() ? "Sudah dibayar" : "Belum dibayar"));
        } else {
            System.out.println("Anda belum memiliki kasus.");
        }
    }
    
    @Override
    public void getDetailPayment() {
        if (currentUser.getCases() != null) {
            Cases userCase = currentUser.getCases();
            System.out.println("Detail Pembayaran:");
            System.out.println("Kasus: " + userCase.getCaseTitle());
            System.out.println("Biaya: Rp " + userCase.getFee());
            System.out.println("Status: " + (userCase.isPaid() ? "Sudah dibayar" : "Belum dibayar"));
        } else {
            System.out.println("Anda belum memiliki kasus untuk dibayar.");
        }
    }
    
    private void makePayment() {
        if (currentUser.getCases() == null) {
            System.out.println("Anda belum memiliki kasus.");
            return;
        }
        
        Cases userCase = currentUser.getCases();
        
        if (userCase.isPaid()) {
            System.out.println("Kasus sudah dibayar.");
            return;
        }
        
        getCase();
        System.out.println("\nBelum melakukan pembayaran.");
        System.out.println("Silakan lakukan pembayaran sebesar: Rp " + userCase.getFee());
        
        if (paymentSystem.processPayment()) {
            userCase.setPaid(true);
            userCase.setStatus("Paid");
            System.out.println("Pembayaran berhasil! Kasus Anda akan segera diproses.");
        }
    }
    
    private void manageCases() {
        System.out.println("\n=== MANAGE CASES ===");
        System.out.println("1. Lihat Semua Kasus");
        System.out.println("2. Tugaskan Pengacara");
        System.out.print("Pilih: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                caseManager.getDetailCase();
                break;
            case 2:
                assignLawyerToCase();
                break;
        }
    }
    
    private void assignLawyerToCase() {
        java.util.List<Cases> cases = ((CaseManager)caseManager).getAllCases();
        if (cases.isEmpty()) {
            System.out.println("Belum ada kasus.");
            return;
        }
        
        System.out.println("Pilih kasus:");
        for (int i = 0; i < cases.size(); i++) {
            Cases c = cases.get(i);
            System.out.println((i+1) + ". " + c.getCaseTitle() + " - " + c.getStatus());
        }
        
        System.out.print("Pilih nomor kasus: ");
        int caseChoice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (caseChoice >= 0 && caseChoice < cases.size()) {
            Cases selectedCase = cases.get(caseChoice);
            
            lawyerManager.getLawyerLayerId();
            System.out.print("Pilih nama pengacara: ");
            String lawyerName = scanner.nextLine();
            
            Lawyer selectedLawyer = ((LawyerManager)lawyerManager).getLawyerByName(lawyerName);
            if (selectedLawyer != null && selectedLawyer.isTersedia()) {
                selectedCase.setLawyerAssigned(selectedLawyer);
                selectedCase.setStatus("Assigned");
                selectedLawyer.setTersedia(false);
                System.out.println("Pengacara berhasil ditugaskan!");
            } else {
                System.out.println("Pengacara tidak tersedia.");
            }
        }
    }
    
    private void manageClient() {
        System.out.println("\n=== MANAGE CLIENT ===");
        clientManager.getClientInfo();
    }
    
    private void manageLawyer() {
        System.out.println("\n=== MANAGE LAWYER ===");
        System.out.println("1. Lihat Daftar Pengacara");
        System.out.println("2. Tambah Pengacara");
        System.out.print("Pilih: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                lawyerManager.getLawyerLayerId();
                break;
            case 2:
                lawyerManager.addLawyer();
                break;
        }
    }
    
    private void checkPayment() {
        System.out.println("\n=== MEMERIKSA PEMBAYARAN ===");
        Payment lastPayment = paymentSystem.getPayment();
        if (lastPayment != null) {
            System.out.println("Pembayaran Terakhir:");
            System.out.println("Jumlah: Rp " + lastPayment.getAmount());
            System.out.println("Status: " + lastPayment.getPaymentStatus());
            System.out.println("Tanggal: " + lastPayment.getPaymentDate());
        } else {
            System.out.println("Belum ada pembayaran.");
        }
    }
    
    private void logout() {
        currentUser = null;
        isAdmin = false;
        System.out.println("Anda telah keluar.");
    }
}