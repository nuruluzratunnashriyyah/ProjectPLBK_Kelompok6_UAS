package business;
import interfaces.*;
import entities.*;
import database.DatabaseManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LawyerHiringSystem implements IMakeHire, ICancelHire, IMakePayment {
    private IClientMgt clientManager;
    private ILawyerMgt lawyerManager;
    private IProcessPayment paymentSystem;
    private ICaseMgt caseManager;
    private List<LawyerHiring> hiringsList;
    
    private Client currentUser;
    private boolean isAdmin;
    private Scanner scanner;
    
    public LawyerHiringSystem() {
        this.clientManager = new ClientManager();
        this.lawyerManager = new LawyerManager();
        this.paymentSystem = new PaymentSystem();
        this.caseManager = new CaseManager();
        this.hiringsList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.isAdmin = false;
        
        loadAllData();
        
        if (clientManager.getClientByEmail("admin@system.com") == null) {
            Client admin = new Client("Admin", "admin@system.com", "admin@system.com", "admin123", "System");
            ((ClientManager)clientManager).addClient(admin);
            DatabaseManager.saveClients(((ClientManager)clientManager).getAllClients());
        }
    }
    
    private void loadAllData() {
        List<Lawyer> lawyers = DatabaseManager.loadLawyers();
        ((LawyerManager)lawyerManager).setLawyers(lawyers);
        
        List<Client> clients = DatabaseManager.loadClients();
        ((ClientManager)clientManager).setClients(clients);
        
        List<Cases> cases = DatabaseManager.loadCases(lawyers, clients);
        ((CaseManager)caseManager).setCases(cases);
        
        ((PaymentSystem)paymentSystem).setPayments(DatabaseManager.loadPayments());
        
        this.hiringsList = DatabaseManager.loadHirings();
    }

    public void start() {
        System.out.println("=== SISTEM HIRING PENGACARA ===");
        
        try {
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
        } finally {
            saveAllData();
        }
    }
    
    private void saveAllData() {
        DatabaseManager.saveClients(((ClientManager)clientManager).getAllClients());
        DatabaseManager.saveLawyers(((LawyerManager)lawyerManager).getAllLawyers());
        DatabaseManager.saveCases(((CaseManager)caseManager).getAllCases());
        DatabaseManager.savePayments(((PaymentSystem)paymentSystem).getAllPayments());
        DatabaseManager.saveHirings(hiringsList);
    }
    
    private void showLoginMenu() {
        while (true) {
            System.out.println("\n1. Daftar");
            System.out.println("2. Masuk");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        register();
                        return;
                    case 2:
                        login();
                        return;
                    case 3:
                        System.out.println("Terima kasih!");
                        System.exit(0);
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
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
        
        if (((ClientManager)clientManager).getClientByEmail(email) != null) {
            System.out.println("Email sudah terdaftar!");
            return;
        }
        
        Client newClient = new Client(nama, email, email, password, alamat);
        boolean added = ((ClientManager)clientManager).addClient(newClient);

        if (added) {
            // Simpan sekali saja
            DatabaseManager.saveClients(((ClientManager)clientManager).getAllClients());
            System.out.println("Pendaftaran berhasil!");
        } else {
            System.out.println("Gagal mendaftarkan pengguna");
        }
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
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Kelola Kasus");
            System.out.println("2. Kelola Klien");
            System.out.println("3. Kelola Pengacara");
            System.out.println("4. Cek Pembayaran");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            
            try {
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
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void showClientMenu() {
        while (true) {
            System.out.println("\n=== MENU CLIENT ===");
            System.out.println("1. Kasus Saya");
            System.out.println("2. Daftar Pengacara");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        showMyCase();
                        break;
                    case 2:
                        viewLawyers();
                        break;
                    case 3:
                        logout();
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void showMyCase() {
        // Dapatkan semua kasus user
        List<Cases> userCases = ((CaseManager)caseManager).getAllCases().stream()
                .filter(c -> c.getClient() != null && currentUser.getEmail().equals(c.getClient().getEmail()))
                .toList();

        // Pisahkan kasus aktif dan selesai
        Cases activeCase = null;
        List<Cases> completedCases = new ArrayList<>();
        
        for (Cases c : userCases) {
            if ("Completed".equals(c.getStatus())) {
                completedCases.add(c);
            } else {
                activeCase = c; // Asumsi hanya ada 1 kasus aktif
            }
        }

        // Tampilkan kasus aktif jika ada
        if (activeCase != null) {
            System.out.println("\n=== KASUS AKTIF ===");
            displayCaseDetails(activeCase);
            
            // Tampilkan kasus selesai jika ada
            if (!completedCases.isEmpty()) {
                System.out.println("\n=== KASUS SELESAI ===");
                for (int i = 0; i < completedCases.size(); i++) {
                    Cases c = completedCases.get(i);
                    System.out.printf("%d. %s - %s - Pengacara %s\n",
                        (i+1), c.getCaseTitle(), c.getStatus(), c.getLawyerAssigned().getNama());
                }
            }
            
            // Menu untuk kasus aktif
            System.out.println("\n1. Batalkan Kasus"); // Selalu tersedia
            if (activeCase.getLawyerAssigned() != null) { // Hanya tampilkan jika ada pengacara
                System.out.println("2. Bayar Kasus");
                System.out.println("3. Kembali");
            } else {
                System.out.println("2. Kembali");
            }
            System.out.print("Pilih: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                if (activeCase.getLawyerAssigned() != null) {
                    switch (choice) {
                        case 1:
                            cancelCase(activeCase);
                            break;
                        case 2:
                            if (!activeCase.isPaid()) {
                                makePayment(activeCase);
                            } else {
                                System.out.println("Kasus sudah dibayar!");
                            }
                            break;
                    }
                } else {
                    if (choice == 1) {
                        cancelCase(activeCase);
                    }
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        } 
        // Jika tidak ada kasus aktif
        else {
            // Tampilkan kasus selesai jika ada
            if (!completedCases.isEmpty()) {
                System.out.println("\n=== KASUS SELESAI ===");
                for (int i = 0; i < completedCases.size(); i++) {
                    Cases c = completedCases.get(i);
                    System.out.printf("%d. %s - %s - Pengacara %s\n",
                        (i+1), c.getCaseTitle(), c.getStatus(), c.getLawyerAssigned().getNama());
                }
            } else {
                System.out.println("\nAnda belum memiliki kasus.");
            }
            
            // Opsi tambah kasus hanya tersedia jika tidak ada kasus aktif
            System.out.println("\n1. Tambah Kasus");
            System.out.println("2. Kembali");
            System.out.print("Pilih: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                if (choice == 1) {
                    addCase();
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void viewLawyers() {
        java.util.List<Lawyer> lawyers = ((LawyerManager)lawyerManager).getAvailableLawyers();
        System.out.println("\nDaftar Pengacara Tersedia:");
        for (int i = 0; i < lawyers.size(); i++) {
            Lawyer l = lawyers.get(i);
            System.out.println((i+1) + ". " + l.getNama() + " - " + l.getSpesialisasi() + 
                " (Fee: Rp " + l.getFeePerCase() + ")");
        }
        
        System.out.println("\n1. Kembali");
        System.out.print("Pilih: ");
        scanner.nextInt();
        scanner.nextLine();
    }

    private void displayCaseDetails(Cases myCase) {
        System.out.println("\n=== KASUS SAYA ===");
        System.out.println("Judul: " + myCase.getCaseTitle());
        System.out.println("Deskripsi: " + myCase.getCaseDescription());
        System.out.println("Status: " + myCase.getStatus());
        System.out.println("Pengacara: " + 
            (myCase.getLawyerAssigned() != null ? 
                myCase.getLawyerAssigned().getNama() : "Belum ditugaskan"));
        System.out.println("Biaya: Rp " + myCase.getFee());
        System.out.println("Status Pembayaran: " + (myCase.isPaid() ? "Sudah dibayar" : "Belum dibayar"));
    }
    
    @Override
    public void addCase() {
        System.out.println("\n=== TAMBAH KASUS ===");
        System.out.print("Judul Kasus: ");
        String title = scanner.nextLine();
        
        System.out.print("Deskripsi Kasus: ");
        String description = scanner.nextLine();
        
        // Deklarasi newCase di awal method
        Cases newCase = new Cases(title, description);
        newCase.setClient(currentUser);
        
        // Proses pemilihan pengacara
        List<Lawyer> availableLawyers = ((LawyerManager)lawyerManager).getAvailableLawyers();
        if (!availableLawyers.isEmpty()) {
            System.out.println("\nDaftar Pengacara Tersedia:");
            for (int i = 0; i < availableLawyers.size(); i++) {
                Lawyer l = availableLawyers.get(i);
                System.out.printf("%d. %s - %s (Fee: Rp %,.2f)\n",
                    (i+1), l.getNama(), l.getSpesialisasi(), l.getFeePerCase());
            }
            
            System.out.print("Pilih pengacara (0 untuk tidak memilih sekarang): ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                if (choice > 0 && choice <= availableLawyers.size()) {
                    Lawyer selected = availableLawyers.get(choice-1);
                    newCase.setLawyerAssigned(selected);
                    newCase.setFee(selected.getFeePerCase());
                    newCase.setStatus("Assigned");
                    selected.setTersedia(false);
                    System.out.println("Pengacara berhasil dipilih!");
                }
            } catch (Exception e) {
                System.out.println("Input tidak valid!");
                scanner.nextLine();
            }
        }
        
        // Simpan kasus baru
        ((CaseManager)caseManager).addCase(newCase);
        DatabaseManager.saveCases(((CaseManager)caseManager).getAllCases());
        System.out.println("Kasus berhasil ditambahkan!");
    }
    
    @Override
    public void cancelCase() {
        if (currentUser == null) {
            System.out.println("Anda harus login terlebih dahulu");
            return;
        }

        Cases myCase = ((CaseManager)caseManager).getClientCase(currentUser.getEmail());
        if (myCase == null) {
            System.out.println("Anda tidak memiliki kasus yang aktif");
            return;
        }

        cancelCase(myCase); // Call the internal method with the case parameter
    }

    // Internal method that handles the actual cancellation
    private void cancelCase(Cases myCase) {
        System.out.print("Apakah yakin ingin membatalkan kasus? (y/n): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("y")) {
            if (myCase.getLawyerAssigned() != null) {
                myCase.getLawyerAssigned().setTersedia(true);
                DatabaseManager.saveLawyers(((LawyerManager)lawyerManager).getAllLawyers());
            }
            
            currentUser.setCases(null);
            ((CaseManager)caseManager).getAllCases().remove(myCase);
            DatabaseManager.saveCases(((CaseManager)caseManager).getAllCases());
            DatabaseManager.saveClients(((ClientManager)clientManager).getAllClients());
            
            System.out.println("Kasus berhasil dibatalkan!");
        } else {
            System.out.println("Pembatalan kasus dibatalkan.");
        }
    }
        
    @Override
    public Cases getCase() {
        if (currentUser == null) {
            System.out.println("Anda harus login terlebih dahulu");
            return null;
        }
        
        Cases myCase = ((CaseManager)caseManager).getClientCase(currentUser.getEmail());
        if (myCase != null) {
            displayCaseDetails(myCase);
        } else {
            System.out.println("Anda belum memiliki kasus.");
        }
        
        return myCase;
    }
    
    @Override
    public void getDetailPayment() {
        Cases myCase = ((CaseManager)caseManager).getClientCase(currentUser.getEmail());
        if (myCase != null) {
            System.out.println("Detail Pembayaran:");
            System.out.println("Kasus: " + myCase.getCaseTitle());
            System.out.println("Biaya: Rp " + myCase.getFee());
            System.out.println("Status: " + (myCase.isPaid() ? "Sudah dibayar" : "Belum dibayar"));
            } else {
            System.out.println("Anda belum memiliki kasus untuk dibayar.");
        }
    }
    
    private void makePayment(Cases myCase) {
        System.out.println("\n=== PEMBAYARAN ===");
        System.out.println("Jumlah yang harus dibayar: Rp " + myCase.getFee());
        
        if (((PaymentSystem)paymentSystem).processPayment()) {
            Payment payment = ((PaymentSystem)paymentSystem).getPayment();
            if (((PaymentSystem)paymentSystem).validatePayment(myCase.getFee(), payment.getAmount())) {
                myCase.setPaid(true);
                myCase.setStatus("Paid");
                DatabaseManager.saveCases(((CaseManager)caseManager).getAllCases());
                DatabaseManager.savePayments(((PaymentSystem)paymentSystem).getAllPayments());
                System.out.println("Pembayaran berhasil!");
            } else {
                System.out.println("Pembayaran gagal!");
            }
        }
    }
    
    private void manageCases() {
        while (true) {
            System.out.println("\n=== KELOLA KASUS ===");
            System.out.println("1. Lihat Semua Kasus");
            System.out.println("2. Tugaskan Pengacara");
            System.out.println("3. Tandai Kasus Selesai");
            System.out.println("4. Kembali");
            System.out.print("Pilih: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        ((CaseManager)caseManager).getDetailCase();
                        break;
                    case 2:
                        assignLawyerToCase();
                        break;
                    case 3:  // Handle opsi baru
                        completeCase();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void completeCase() {
        List<Cases> paidCases = ((CaseManager)caseManager).getAllCases().stream()
            .filter(c -> "Paid".equalsIgnoreCase(c.getStatus()))
            .toList();

        if (paidCases.isEmpty()) {
            System.out.println("Tidak ada kasus dengan status Paid");
            return;
        }

        System.out.println("\nDaftar Kasus yang bisa ditandai selesai:");
        for (int i = 0; i < paidCases.size(); i++) {
            Cases c = paidCases.get(i);
            System.out.printf("%d. %s - Klien: %s\n",
                (i+1), c.getCaseTitle(),
                c.getClient() != null ? c.getClient().getNama() : "Tidak ada");
        }

        System.out.print("Pilih nomor kasus (0 untuk batal): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice == 0) return;
            
            if (choice < 1 || choice > paidCases.size()) {
                System.out.println("Nomor tidak valid!");
                return;
            }

            Cases selectedCase = paidCases.get(choice-1);
            System.out.print("Apakah yakin ingin menandai kasus '" + selectedCase.getCaseTitle() + "' sebagai selesai? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                selectedCase.setStatus("Completed");
                
                // Bebaskan pengacara jika ada
                if (selectedCase.getLawyerAssigned() != null) {
                    selectedCase.getLawyerAssigned().setTersedia(true);
                    DatabaseManager.saveLawyers(((LawyerManager)lawyerManager).getAllLawyers());
                }
                
                DatabaseManager.saveCases(((CaseManager)caseManager).getAllCases());
                System.out.println("Kasus berhasil ditandai selesai!");
            } else {
                System.out.println("Kasus tetap berstatus Paid.");
            }
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
            scanner.nextLine();
        }
    }
    public boolean hasActiveHirings(Lawyer lawyer) {
        for (LawyerHiring hiring : hiringsList) {
            // Check if the hiring record is for this lawyer and still active
            for (Cases caseItem : ((CaseManager)caseManager).getAllCases()) {
                if (caseItem.getLawyerAssigned() != null && 
                    caseItem.getLawyerAssigned().equals(lawyer) && 
                    hiring.getCaseStatus().equals("Active")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void assignLawyerToCase() {
        List<Cases> cases = ((CaseManager)caseManager).getAllCases();
        if (cases.isEmpty()) {
            System.out.println("Belum ada kasus.");
            return;
        }
        
        while (true) {
            System.out.println("\nPilih kasus:");
            for (int i = 0; i < cases.size(); i++) {
                Cases c = cases.get(i);
                if ("Paid".equalsIgnoreCase(c.getStatus())) {
                    System.out.println("Tidak ada kasus yang belum diberikan pengacara.");
                    continue;
                }
                System.out.printf("%d. %s - %s (Klien: %s)\n",
                    (i+1), c.getCaseTitle(), c.getStatus(), 
                    c.getClient() != null ? c.getClient().getNama() : "Tidak ada");
            }
            
            System.out.println("0. Kembali ke menu sebelumnya");
            System.out.print("Pilih nomor kasus: ");
            
            try {
                int caseChoice = scanner.nextInt();
                scanner.nextLine();
                
                if (caseChoice == 0) {
                    return; // Kembali ke menu manage cases
                }
                
                if (caseChoice < 1 || caseChoice > cases.size()) {
                    System.out.println("Nomor tidak valid!");
                    continue;
                }
                
                Cases selectedCase = cases.get(caseChoice-1);
                assignLawyerToSpecificCase(selectedCase);
                return;
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void assignLawyerToSpecificCase(Cases selectedCase) {
        List<Lawyer> availableLawyers = ((LawyerManager)lawyerManager).getAvailableLawyers();

        if (availableLawyers.isEmpty()) {
            System.out.println("Tidak ada pengacara tersedia.");
            return;
        }
        
        System.out.println("\nPilih pengacara:");
        for (int i = 0; i < availableLawyers.size(); i++) {
            Lawyer l = availableLawyers.get(i);
            System.out.printf("%d. %s - %s (Fee: Rp %,.2f)\n",
                (i+1), l.getNama(), l.getSpesialisasi(), l.getFeePerCase());
        }
        
        System.out.print("Pilih nomor pengacara (0 untuk batal): ");
        try {
            int lawyerChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (lawyerChoice == 0) return;
            
            if (lawyerChoice < 1 || lawyerChoice > availableLawyers.size()) {
                System.out.println("Nomor tidak valid!");
                return;
            }
            
            Lawyer selectedLawyer = availableLawyers.get(lawyerChoice-1);

            // Call the updated method
            ((CaseManager)caseManager).assignLawyerToCase(selectedCase, selectedLawyer);
            
            // Add to hiring records
            LawyerHiring newHiring = new LawyerHiring(
                "Active", 
                java.time.LocalDate.now().toString(),
                selectedCase.getClient().getEmail(),
                selectedLawyer.getNama()
            );
            
            hiringsList.add(newHiring);
            
            // Save changes
            DatabaseManager.saveCases(((CaseManager)caseManager).getAllCases());
            DatabaseManager.saveLawyers(((LawyerManager)lawyerManager).getAllLawyers());
            DatabaseManager.saveHirings(hiringsList);
            
            System.out.println("Pengacara berhasil ditugaskan!");
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
            scanner.nextLine();
        }
    }

    
    private void manageClient() {
        while (true) {
            System.out.println("\n=== KELOLA CLIENT ===");
            System.out.println("1. Lihat Daftar Client");
            System.out.println("2. Hapus Client");
            System.out.println("3. Kembali");
            System.out.print("Pilih: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        clientManager.getClientInfo();
                        break;
                    case 2:
                        deleteClient();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void deleteClient() {
        List<Client> clients = ((ClientManager)clientManager).getAllClients();
        if (clients.isEmpty()) {
            System.out.println("\nBelum ada klien yang bisa dihapus.");
            return;
        }
        
        // Tampilkan daftar klien dengan numbering
        System.out.println("\nDaftar Klien:");
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            System.out.printf("%d. %s - %s%n", (i+1), c.getNama(), c.getEmail());
        }
        
        System.out.print("\nPilih nomor klien yang akan dihapus (0 untuk batal): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice == 0) return;
            
            if (choice < 1 || choice > clients.size()) {
                System.out.println("Nomor tidak valid!");
                return;
            }
            
            Client toDelete = clients.get(choice-1);
            
            // Cek jika klien adalah admin
            if (toDelete.getEmail().equals("admin@system.com")) {
                System.out.println("Admin tidak boleh dihapus!");
                return;
            }
        
            // Cek apakah klien memiliki kasus aktif
            if (hasActiveCases(toDelete)) {
                System.out.println("Klien tidak dapat dihapus karena masih memiliki kasus aktif!");
                return;
            }
            
            System.out.print("Apakah yakin ingin menghapus klien " + toDelete.getNama() + "? (y/n): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("y")) {
                // Hapus dari list dan simpan
                clients.remove(choice-1);
                ((ClientManager)clientManager).setClients(clients);
                DatabaseManager.saveClients(clients);
                System.out.println("Klien berhasil dihapus!");
            } else {
                System.out.println("Penghapusan klien dibatalkan.");
            }
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
            scanner.nextLine();
        }
    }

    private boolean hasActiveCases(Client client) {
        List<Cases> allCases = ((CaseManager)caseManager).getAllCases();
        return allCases.stream()
            .anyMatch(c -> c.getClient() != null 
                        && c.getClient().getEmail().equals(client.getEmail()) 
                        && !"Completed".equals(c.getStatus()));
    }

    private void manageLawyer() {
        while (true) {
            System.out.println("\n=== KELOLA PENGACARA ===");
            System.out.println("1. Lihat Daftar Pengacara");
            System.out.println("2. Tambah Pengacara");
            System.out.println("3. Hapus Pengacara");
            System.out.println("4. Kembali");
            System.out.print("Pilih: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        viewAllLawyers();
                        break;
                    case 2:
                        ((LawyerManager)lawyerManager).addLawyer();
                        DatabaseManager.saveLawyers(((LawyerManager)lawyerManager).getAllLawyers());
                        break;
                    case 3:
                        deleteLawyer();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Input harus angka!");
                scanner.nextLine();
            }
        }
    }
    
    private void viewAllLawyers() {
        List<Lawyer> lawyers = ((LawyerManager)lawyerManager).getAllLawyers();
        System.out.println("\nDaftar Semua Pengacara:");
        for (int i = 0; i < lawyers.size(); i++) {
            Lawyer l = lawyers.get(i);
            System.out.printf("%d. %s - %s (Fee: Rp %,.2f) (%s)\n",
                (i+1), l.getNama(), l.getSpesialisasi(), 
                l.getFeePerCase(), l.isTersedia() ? "Tersedia" : "Tidak Tersedia");
        }
    }
    
    private void deleteLawyer() {
        List<Lawyer> lawyers = ((LawyerManager)lawyerManager).getAllLawyers();
        viewAllLawyers();
        
        System.out.print("Pilih nomor pengacara yang akan dihapus (0 untuk batal): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice == 0) return;
            
            if (choice < 1 || choice > lawyers.size()) {
                System.out.println("Nomor tidak valid!");
                return;
            }
            
            Lawyer toDelete = lawyers.get(choice-1);
            
            if (!toDelete.isTersedia()) {
                System.out.println("Pengacara tidak dapat dihapus karena sedang menangani kasus!");
                return;
            }
            
            lawyers.remove(choice-1);
            DatabaseManager.saveLawyers(lawyers);
            System.out.println("Pengacara berhasil dihapus!");
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
            scanner.nextLine();
        }
    }
    
    private void checkPayment() {
        System.out.println("\n=== MEMERIKSA PEMBAYARAN ===");
        Payment lastPayment = paymentSystem.getPayment();
        if (lastPayment != null) {
            System.out.println("Pembayaran Terakhir: " );
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