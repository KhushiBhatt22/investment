package com.example.investment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SavingRecord {
    @Id
    private int customerNumber;
    private String customerName;
    private double customerDeposit;
    private int numberOfYears;
    private String savingType; //saving deluxe and savings regular

    public SavingRecord() {
    }

    public SavingRecord(int customerNumber, String customerName, double customerDeposit, int numberOfYears, String savingType) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.customerDeposit = customerDeposit;
        this.numberOfYears = numberOfYears;
        this.savingType = savingType;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getCustomerDeposit() {
        return customerDeposit;
    }

    public void setCustomerDeposit(double customerDeposit) {
        this.customerDeposit = customerDeposit;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public String getSavingType() {
        return savingType;
    }

    public void setSavingType(String savingType) {
        this.savingType = savingType;
    }
}
