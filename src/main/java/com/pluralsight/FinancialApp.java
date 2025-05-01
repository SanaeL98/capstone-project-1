package com.pluralsight;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FinancialApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMainMenu();
            String selectMenuOption = scanner.nextLine().toUpperCase();

            switch (selectMenuOption) {
                case "D":
                    makeADeposit();
                    break;
                case "P":
                    makeAPayment();
                    break;
                case "L":
                    showLedgerMenu();
                    break;
                case "R":
                    showReportsMenu();
                    break;
                case "X":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

    }

    public static void showMainMenu() {
        System.out.println("\nWelcome to the accounting Ledger App! ");
        System.out.println("Please choose an option");
        System.out.println("D) Add deposit");
        System.out.println("P) Make Payment");
        System.out.println("L) View Ledger");
        System.out.println("R) Run Report");
        System.out.println("X) Exit");
        System.out.print("Your choice: ");
    }


    public static void showLedgerMenu() {
        boolean inLedgerMenu = true;

        while (inLedgerMenu) {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("1) Display All Entries");
            System.out.println("2) Display Deposits");
            System.out.println("3) Display Payments");
            System.out.println("4) Back to Main Menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayAllEntries();
                    break;
                case "2":
                    displayDeposits();
                    break;
                case "3":
                    displayPayments();
                    break;
                case "4":
                    inLedgerMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void showReportsMenu() {
        boolean inReportsMenu = true;

        while (inReportsMenu) {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1) Display by Month");
            System.out.println("2) Display Previous Month");
            System.out.println("3) Display Year to Date");
            System.out.println("4) Search by Vendor");
            System.out.println("5) Back to Main Menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayPayments();
                    break;
                case "2":
                    displayPreviousMonth();
                    break;
                case "3":
                    displayYearToDate();
                    break;
                case "4":
                    searchByVendor();
                    break;
                case "5":
                    inReportsMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void makeADeposit() {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            LocalDateTime now = LocalDateTime.now();

            Ledger deposit = new Ledger(now, description, vendor, amount);

            FileWriter writer = new FileWriter("data/transactions.csv", true);
            writer.write(now.toLocalDate() + "|" + now.toLocalTime() + "|" + deposit.getDescription() + "|" + deposit.getVendor() + "|" + deposit.getAmount() + "\n");
            writer.close();

            System.out.println("Deposit saved successfully.");

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }


    public static void makeAPayment() {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            amount = -Math.abs(amount);

            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            FileWriter writer = new FileWriter("data/transactions.csv", true);
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount + "\n");
            writer.close();

            System.out.println("Payment recorded successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    public static void displayAllEntries() {
        try (Scanner fileScanner = new Scanner(new java.io.File("data/transactions.csv"))) {
            System.out.println("\n=== All Ledger Entries ===");

            fileScanner.nextLine(); // Skip the header row

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading entries: " + e.getMessage());
        }
    }


    public static void displayDeposits() {
        try (Scanner fileScanner = new Scanner(new java.io.File("data/transactions.csv"))) {
            System.out.println("\n=== Deposits ===");

            fileScanner.nextLine(); // Skip the header row

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);
                if (amount > 0) {
                    System.out.println(line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading deposits: " + e.getMessage());
        }

    }


    public static void displayPayments() {
        try (Scanner fileScanner = new Scanner(new java.io.File("data/transactions.csv"))) {
            System.out.println("\n=== Payments ===");

            fileScanner.nextLine(); // Skip the header row

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);
                if (amount < 0) {
                    System.out.println(line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading payments: " + e.getMessage());
        }
    }

    public static void displayByMonth() {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        try (Scanner fileScanner = new Scanner(new File("data/transactions.csv"))) {
            System.out.println("\n=== Transactions for This Month ===");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.toLowerCase().contains("amount")) continue;
                String[] parts = line.split("\\|");
                if (parts[0].startsWith(currentMonth)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

    public static void displayPreviousMonth() {
        String lastMonth = LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        try (Scanner fileScanner = new Scanner(new File("data/transactions.csv"))) {
            System.out.println("\n=== Transactions for Previous Month ===");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.toLowerCase().contains("amount")) continue;
                String[] parts = line.split("\\|");
                if (parts[0].startsWith(lastMonth)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

    public static void displayYearToDate() {
        String currentYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        try (Scanner fileScanner = new Scanner(new File("data/transactions.csv"))) {
            System.out.println("\n=== Year-to-Date Transactions ===");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.toLowerCase().contains("amount")) continue;
                String[] parts = line.split("\\|");
                if (parts[0].startsWith(currentYear)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

    public static void searchByVendor() {
        System.out.print("Enter vendor name to search: ");
        String searchVendor = scanner.nextLine().toLowerCase();

        try (Scanner fileScanner = new Scanner(new File("data/transactions.csv"))) {
            System.out.println("\n=== Transactions for Vendor: " + searchVendor + " ===");
            boolean found = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.toLowerCase().contains("amount")) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String vendor = parts[3].toLowerCase();
                    if (vendor.contains(searchVendor)) {
                        System.out.println(line);
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("No transactions found for vendor: " + searchVendor);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

}



