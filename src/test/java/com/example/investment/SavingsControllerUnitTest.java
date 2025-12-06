package com.example.investment;

//import ch.qos.logback.core.model.Model;
import com.example.investment.controller.SavingsController;
import com.example.investment.model.SavingRecord;
import com.example.investment.repository.SavingRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.Model;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This is a UNIT TEST
// It tests the SavingsController in isolation using Mockito.
@ExtendWith(MockitoExtension.class)
public class SavingsControllerUnitTest {

    // Mock the dependency (the Repository)
    @Mock
    private SavingRecordRepository savingsRecordRepository;

    // Inject the mocks into the class under test (the Controller)
    @InjectMocks
    private SavingsController savingsController;

    // Mock the Model and RedirectAttributes objects needed for the methods
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;

    // --- Test 3: Check Redirect on Duplicate Add (Unit Test - Requirement 4 logic) ---
    @Test
    void testAddRecord_DuplicateCustomer_ReturnsRedirect() {
        // GIVEN: A record that already exists
        SavingRecord existingRecord = new SavingRecord(115, "Test Name", 1000.0, 1, "Savings-Deluxe");

        // WHEN: Mock the repository to return TRUE (indicating the record exists)

        when(savingsRecordRepository.existsById(115)).thenReturn(true);

        // ACT
        String viewName = savingsController.addRecord(existingRecord, redirectAttributes);

        // THEN
        // Verify that the save method was NOT called (we prevented the save)
        verify(savingsRecordRepository, never()).save(any(SavingRecord.class));
        // Verify that a flash attribute message was added
        verify(redirectAttributes).addFlashAttribute(eq("message"), anyString());
        // Verify that it redirects to the main page
        assertEquals("redirect:/", viewName);
    }

    // --- Bonus Test: Test Showing All Records (Unit Test - Read logic) ---
    @Test
    void testShowAllRecords_LoadsData() {
        // GIVEN: A list of records
        List<SavingRecord> records = new ArrayList<>();
        records.add(new SavingRecord(101, "Test", 1000, 1, "Savings-Regular"));

        // WHEN: Mock the repository to return the list
        when(savingsRecordRepository.findAll()).thenReturn(records);

        // ACT
        String viewName = savingsController.showAllRecords(model);

        // THEN
        // Verify that the repository's findAll was called
        verify(savingsRecordRepository, times(1)).findAll();
        // Verify that the list of records was added to the model
        verify(model).addAttribute("records", records);
        // Verify that it returns the correct view name
        assertEquals("customer", viewName);
    }

    // --- Bonus Test: Test Show Edit Form (Unit Test - Edit logic) ---
    @Test
    void testShowEditForm_ExistingId_ReturnsEditView() {
        // GIVEN: An existing record
        SavingRecord existingRecord = new SavingRecord(115, "Test Name", 1000.0, 1, "Savings-Deluxe");

        // WHEN: Mock the repository to return the record
        when(savingsRecordRepository.findById(115)).thenReturn(Optional.of(existingRecord));

        // ACT
        String viewName = savingsController.showEditForm(115, model, redirectAttributes);

        // THEN
        // Verify that the record was added to the model
        verify(model).addAttribute("savingsRecord", existingRecord);
        // Verify that it returns the edit view
        assertEquals("edit", viewName);
    }
}