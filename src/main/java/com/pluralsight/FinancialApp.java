package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
                    viewLedger();
                    break;
                case "R":
                    runReport();
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
        System.out.println("L) Ledger");
        System.out.println("R) Run Report");
        System.out.println("X) Exit");
        System.out.println("Your choice: ");
    }

    public static void makeADeposit() {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            LocalDateTime dateTime = LocalDateTime.now();
            String date = dateTime.toLocalDate().toString();
            String time = dateTime.toLocalTime().withNano(0).toString();

            String[] tokens = {date, time, description, vendor, String.valueOf(amount)};

            String transaction = String.join(",", tokens);

            FileWriter writer = new FileWriter("data/transactions.csv", true);
            writer.write(transaction + "\n");
            writer.close();

            System.out.println("Deposit saved successfully!");

        } catch (IOException e) {
            System.out.println("An error occurred while saving the deposit: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered for amount.");
        }
    }

    public static void makeAPayment() {
        System.out.println("\nMake Payment");
        System.out.println("------------");

        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            amount = -Math.abs(amount);

            LocalDateTime dateTime = LocalDateTime.now();
            String date = dateTime.toLocalDate().toString();
            String time = dateTime.toLocalTime().withNano(0).toString();

            String[] tokens = {date, time, description, vendor, String.valueOf(amount)};
            String transaction = String.join(",", tokens);

            FileWriter writer = new FileWriter("data/transactions.csv", true);
            writer.write(transaction + "\n");
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void viewLedger() {

        System.out.println("\n--- Ledger Entries ---\n");

        List<Ledger> entries = new ArrayList<>();

        try {
          //  BufferedReader reader = new BufferedReader(new FileReader("data/transactions.csv"));
            FileReader fileReader = new FileReader("data/transactions.csv");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                LocalDateTime dateTime = LocalDateTime.parse(tokens[0] + "T" + tokens[1]);
                String description = tokens[2];
                String vendor = tokens[3];
                double amount = Double.parseDouble(tokens[4]);

                Ledger entry = new Ledger(dateTime, description, vendor, amount);
                entries.add(entry);
            }
            reader.close();

            entries.sort(Comparator.comparing(Ledger::getDateTime).reversed());

            for (Ledger entry : entries) {
                System.out.println(entry.toString());
            }
        } catch (IOException e) {
            //System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //System.out.println("Invalid number format in file.");
            e.printStackTrace();
        }

        promptReturnToMenu();
    }

    public static void promptReturnToMenu() {
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();

    }

    public static void runReport() {
    boolean reporting = true;

    while (reporting) {
        System.out.println("\nRun Reports");
        System.out.println("-----------");
        System.out.println("1) Show deposits only");
        System.out.println("2) Show payments only");
        System.out.println("3) Search by vendor");
        System.out.println("4) Return to main menu");
        System.out.print("Enter option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                showDeposit();
                break;
            case "2":
                showPayments();
                break;
            case "3":
                searchByVendor();
                break;
            case "4":
                reporting = false;
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }
}


public static void showDeposit() {
    System.out.println("\n--- Deposits ---");

    try {
        BufferedReader reader = new BufferedReader(new FileReader("data/transactions.csv"));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");

            double amount = Double.parseDouble(tokens[4]);

            if (amount > 0) {
                System.out.println(line);
            }
        }

        reader.close();
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format in file.");

    }

    promptReturnToMenu();
}

public static void showPayments() {
    System.out.println("\n--- Payments ---");

    try {
        BufferedReader reader = new BufferedReader(new FileReader("data/transactions.csv"));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            double amount = Double.parseDouble(tokens[4]);

            if (amount < 0) {
                System.out.println(line);
            }
        }

        reader.close();
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Invalid number format in file.");
    }

    promptReturnToMenu();
}


public static void searchByVendor() {
    System.out.print("\nEnter vendor name to search: ");
    String searchVendor = scanner.nextLine().toLowerCase();

    System.out.println("\n--- Results for Vendor: " + searchVendor + " ---");

    try {
        BufferedReader reader = new BufferedReader(new FileReader("data/transactions.csv"));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            String vendor = tokens[3].toLowerCase();

            if (vendor.contains(searchVendor)) {
                System.out.println(line);
            }
        }

        reader.close();
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }

    promptReturnToMenu();
}
    }

