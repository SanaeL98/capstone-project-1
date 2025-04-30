package com.pluralsight;

import java.time.LocalDateTime;

public class Ledger {
        private LocalDateTime dateTime;
        private String description;
        private String vendor;
        private Double amount;

        public Ledger(LocalDateTime dateTime, String description, String vendor, double amount) {
            this.dateTime = dateTime;
            this.description = description;
            this.vendor = vendor;
            this.amount = amount;
        }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return dateTime + "," + description + "," + vendor + "," + amount;
    }
}

