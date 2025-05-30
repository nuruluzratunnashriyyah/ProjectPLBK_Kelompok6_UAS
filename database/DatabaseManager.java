package database;

import entities.*;
import java.io.*;
import java.util.*;

public class DatabaseManager {
    public static List<Client> loadClients() {
        List<Client> clients = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("clients.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 5) {
                    Client client = new Client(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    clients.add(client);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Clients file not found, will be created later");
        }
        return clients;
    }

    public static List<Lawyer> loadLawyers() {
        List<Lawyer> lawyers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("lawyers.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 4) {
                    Lawyer lawyer = new Lawyer(parts[0], parts[1], Double.parseDouble(parts[2]));
                    lawyer.setTersedia(Boolean.parseBoolean(parts[3]));
                    lawyers.add(lawyer);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Lawyers file not found, will be created later");
        }
        return lawyers;
    }

    public static List<Cases> loadCases(List<Lawyer> lawyers, List<Client> clients) {
        List<Cases> cases = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("cases.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 7) {
                    Cases newCase = new Cases(parts[0], parts[1]);
                    newCase.setStatus(parts[3]);
                    newCase.setFee(Double.parseDouble(parts[4]));
                    newCase.setPaid(Boolean.parseBoolean(parts[5]));
                    
                    if (!parts[2].equals("null")) {
                        Lawyer lawyer = findLawyerByName(lawyers, parts[2]);
                        newCase.setLawyerAssigned(lawyer);
                    }
                    
                    if (!parts[6].equals("null")) {
                        Client client = findClientByEmail(clients, parts[6]);
                        newCase.setClient(client);
                        client.setCases(newCase);
                    }
                    
                    cases.add(newCase);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cases file not found, will be created later");
        }
        return cases;
    }

    public static void saveClients(List<Client> clients) {
        try (PrintWriter writer = new PrintWriter("clients.txt")) {
            for (Client c : clients) {
                writer.println(String.join(",",
                    c.getNama(),
                    c.getKontak(),
                    c.getEmail(),
                    c.getPassword(),
                    c.getAlamat()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving clients: " + e.getMessage());
        }
    }

    public static void saveLawyers(List<Lawyer> lawyers) {
        try (PrintWriter writer = new PrintWriter("lawyers.txt")) {
            for (Lawyer l : lawyers) {
                writer.println(String.join(",",
                    l.getNama(),
                    l.getSpesialisasi(),
                    String.valueOf(l.getFeePerCase()),
                    String.valueOf(l.isTersedia())
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving lawyers: " + e.getMessage());
        }
    }

    public static void saveCases(List<Cases> cases) {
        try (PrintWriter writer = new PrintWriter("cases.txt")) {
            for (Cases c : cases) {
                writer.println(String.join(",",
                    c.getCaseTitle(),
                    c.getCaseDescription(),
                    c.getLawyerAssigned() != null ? c.getLawyerAssigned().getNama() : "null",
                    c.getStatus(),
                    String.valueOf(c.getFee()),
                    String.valueOf(c.isPaid()),
                    c.getClient() != null ? c.getClient().getEmail() : "null"
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving cases: " + e.getMessage());
        }
    }

    private static Lawyer findLawyerByName(List<Lawyer> lawyers, String name) {
        return lawyers.stream()
            .filter(l -> l.getNama().equals(name))
            .findFirst()
            .orElse(null);
    }

    private static Client findClientByEmail(List<Client> clients, String email) {
        return clients.stream()
            .filter(c -> c.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }

    public static List<Payment> loadPayments() {
        List<Payment> payments = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("payments.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 3) {
                    double amount = Double.parseDouble(parts[0]);
                    String status = parts[1];
                    String date = parts[2];
                    payments.add(new Payment(amount, status, date));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Payment file not found, will be created later");
        } catch (Exception e) {
            System.out.println("Error loading payments: " + e.getMessage());
        }
        return payments;
    }

    public static void savePayments(List<Payment> payments) {
        try (PrintWriter writer = new PrintWriter("payments.txt")) {
            for (Payment p : payments) {
                writer.println(p.getAmount() + "," + p.getPaymentStatus() + "," + p.getPaymentDate());
            }
        } catch (Exception e) {
            System.out.println("Error saving payments: " + e.getMessage());
        }
    }
    
    public static List<LawyerHiring> loadHirings() {
        List<LawyerHiring> hirings = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("hirings.txt"))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 4) {
                    hirings.add(new LawyerHiring(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Hirings file not found, will be created later");
        }
        return hirings;
    }

    public static void saveHirings(List<LawyerHiring> hirings) {
        try (PrintWriter writer = new PrintWriter("hirings.txt")) {
            for (LawyerHiring lh : hirings) {
                writer.println(String.join(",",
                    lh.getCaseStatus(),
                    lh.getHiringDate(),
                    lh.getClientEmail(),
                    lh.getLawyerName()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error saving hirings: " + e.getMessage());
        }
    }

    public static boolean hasActiveHiringsForLawyer(Lawyer lawyer, List<Cases> cases, List<LawyerHiring> hirings) {
        for (Cases c : cases) {
            if (c.getLawyerAssigned() != null && c.getLawyerAssigned().equals(lawyer)) {
                for (LawyerHiring hiring : hirings) {
                    if (hiring.getCaseStatus().equals("Active")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}