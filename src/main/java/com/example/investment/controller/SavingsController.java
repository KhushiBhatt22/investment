package com.example.investment.controller;

import com.example.investment.model.ProjectionEntry;
import com.example.investment.model.SavingRecord;
import com.example.investment.repository.SavingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SavingsController {

    // Dependency Injection (Autowiring) - Requirement 10
    @Autowired
    private SavingRecordRepository savingsRecordRepository;

    // Handler for the main page
    @GetMapping("/")
    public String showAllRecords(Model model) {
        // Fetch all savings records from the database
        model.addAttribute("records", savingsRecordRepository.findAll());
        // Set an empty message for the error/success banner (will be used in Step 4)
        model.addAttribute("message", "");

        // This returns the 'customer.html' template
        return "customer";
    }

    // 1. Handler for showing the Add Form (Requirement 2)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Pass a new, empty SavingsRecord object to bind the form fields to.
        model.addAttribute("savingsRecord", new SavingRecord());
        return "add"; // Returns the add.html template
    }

    // 2. Handler for processing the form submission (Requirement 3 & 4)
    @PostMapping("/add")
    public String addRecord(@ModelAttribute SavingRecord savingsRecord,
                            RedirectAttributes redirectAttributes) {

        // Check if the record already exists (Requirement 4)
        if (savingsRecordRepository.existsById(savingsRecord.getCustomerNumber())) {

            // Error message for existing record (Requirement 4)
            redirectAttributes.addFlashAttribute("message",
                    "The record you are trying to add is already existing. Choose a different customer number");

            // Redirect back to the main page to show the error
            return "redirect:/";

        } else {
            // Save the new record
            savingsRecordRepository.save(savingsRecord);

            // Success message (Requirement 3)
            redirectAttributes.addFlashAttribute("message",
                    "Success! New savings record added for Customer #" + savingsRecord.getCustomerNumber() + ".");

            // Upon successful submission, display the main page with the new entry (Requirement 3)
            return "redirect:/";
        }
    }
    // 1. Handler for showing the Edit Form (Requirement 6 & 138)
// Fetches the existing record by ID and populates the form[cite: 138].
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Use findById, which returns an Optional
        return savingsRecordRepository.findById(id)
                .map(record -> {
                    // Found the record, add it to the model to automatically fill out the textboxes[cite: 138].
                    model.addAttribute("savingsRecord", record);
                    return "edit"; // Returns the edit.html template
                })
                .orElseGet(() -> {
                    // Record not found (shouldn't happen if clicking from the main page)
                    redirectAttributes.addFlashAttribute("message", "Error: Record not found for editing.");
                    return "redirect:/";
                });
    }

    // 2. Handler for processing the form update (Requirement 7)
    @PostMapping("/update")
    public String updateRecord(@ModelAttribute SavingRecord savingsRecord, RedirectAttributes redirectAttributes) {

        // JPA's save() method acts as an update if the primary key (customerNumber) already exists.
        savingsRecordRepository.save(savingsRecord);

        // Success message
        // The edited record will be displayed after the user clicked the edit button[cite: 140].
        redirectAttributes.addFlashAttribute("message",
                "Success! Customer #" + savingsRecord.getCustomerNumber() + " was successfully updated.");

        // Upon submission, display the main page with the updated entry[cite: 140].
        return "redirect:/";
    }

    // Note: We use POST for delete operations to follow RESTful security conventions
    @PostMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Delete the record by its primary key (customerNumber)
            savingsRecordRepository.deleteById(id);

            // Success message
            redirectAttributes.addFlashAttribute("message",
                    "Success! Customer #" + id + " was successfully deleted.");

        } catch (Exception e) {
            // Error handling in case the record doesn't exist or a database error occurs
            redirectAttributes.addFlashAttribute("message",
                    "Error: Could not delete record #" + id + ".");
        }

        // Redirect back to the main page to show the updated list
        return "redirect:/";
    }

    // Handler for showing the Projected Investment table (Requirement 9)
    @GetMapping("/projected-investment/{id}")
    public String showProjectedInvestment(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

        return savingsRecordRepository.findById(id)
                .map(record -> {
                    // Get necessary data from the record
                    String name = record.getCustomerName();
                    double initialDeposit = record.getCustomerDeposit();
                    int years = record.getNumberOfYears();
                    String type = record.getSavingType();

                    // Determine the annual interest rate based on savings type
                    double rate = 0.0;
                    if (type.equals("Savings-Deluxe")) {
                        // The interest in Savings De-luxe is 15% a year.
                        rate = 0.15;
                    } else if (type.equals("Savings-Regular")) {
                        // The interest in Savings Regular is 10% a year
                        rate = 0.10;
                    }

                    // List to hold the projection data (Year, Starting Amount, Interest, Ending Balance)
                    List<ProjectionEntry> projectionTable = new ArrayList<>();
                    double currentBalance = initialDeposit;

                    // Loop from year 1 up to the number of years limit [cite: 247]
                    for (int year = 1; year <= years; year++) {
                        // Calculate Interest: based on the starting amount (compound interest)
                        double interest = currentBalance * rate;
                        // Ending Balance: accumulated amount plus interest
                        double endingBalance = currentBalance + interest;

                        // Add the entry to the list
                        projectionTable.add(new ProjectionEntry(
                                year,
                                currentBalance,
                                interest,
                                endingBalance
                        ));

                        // Set the starting amount for the next year
                        currentBalance = endingBalance;
                    }

                    // Add required attributes to the model
                    model.addAttribute("customerNumber", id);
                    model.addAttribute("customerName", name);
                    model.addAttribute("projectionTable", projectionTable);

                    return "projection"; // Returns the projection.html template
                })
                .orElseGet(() -> {
                    // Record not found
                    redirectAttributes.addFlashAttribute("message", "Error: Projected Investment record not found.");
                    return "redirect:/";
                });
    }

}