Financial Ledger Application 

This is a command-line Java application that acts as a basic accounting ledger. It allows users to:
Add deposits and payments
	•	View all ledger entries
	•	Filter transactions by deposits, payments, date ranges, or vendor
	•	Store and retrieve data from a CSV file
 
ScreenShots:

 ![Main menu](https://github.com/user-attachments/assets/c92c1b2a-e1dc-47c5-9631-40074e56077a)
![Make a deposit](https://github.com/user-attachments/assets/4264b685-f246-4849-9a84-65b25ea95497)
![View Ledger Entries](https://github.com/user-attachments/assets/504e4069-0014-4183-8b54-cb615e1d9407)

Interesting Code Example

One interesting part of the project is how it filters transactions by vendor; 

public static void searchByVendor() {
    System.out.print("Enter vendor name to search: ");
    String searchVendor = scanner.nextLine().toLowerCase();

    try (Scanner fileScanner = new Scanner(new File("data/transactions.csv"))) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.toLowerCase().contains("amount")) continue;
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                String vendor = parts[3].toLowerCase();
                if (vendor.contains(searchVendor)) {
                    System.out.println(line);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
}

Why; 
It uses .contains() and .toLowerCase() to allow flexible searching. The user can type part of a vendor’s name, and it will find any matches in the CSV file.

 

