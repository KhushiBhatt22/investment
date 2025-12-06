package com.example.investment.model;

// Simple class to hold the calculated row data for the projection table
public class ProjectionEntry {
    private int year;
    private double startingAmount;
    private double interest;
    private double endingBalance;

    public ProjectionEntry(int year, double startingAmount, double interest, double endingBalance) {
        this.year = year;
        this.startingAmount = startingAmount;
        this.interest = interest;
        this.endingBalance = endingBalance;
    }

    // --- Getters ---
    public int getYear() { return year; }
    public double getStartingAmount() { return startingAmount; }
    public double getInterest() { return interest; }
    public double getEndingBalance() { return endingBalance; }
}