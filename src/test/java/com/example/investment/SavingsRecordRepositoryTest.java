package com.example.investment;

import com.example.investment.model.SavingRecord;
import com.example.investment.repository.SavingRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This is an INTEGRATION TEST
// It tests the interaction between the Repository and the in-memory H2 database.
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SavingsRecordRepositoryTest {

    @Autowired
    private SavingRecordRepository repository;

    // Test 1: Save and Find By ID (Integration Test)
    @Test
    void testSaveAndFindRecord() {
        // GIVEN
        SavingRecord record = new SavingRecord(901, "Test Customer A", 1000.0, 3, "Savings-Regular");

        // WHEN
        SavingRecord savedRecord = repository.save(record);

        // THEN
        Optional<SavingRecord> foundRecord = repository.findById(savedRecord.getCustomerNumber());
        assertTrue(foundRecord.isPresent());
        assertThat(foundRecord.get().getCustomerName()).isEqualTo("Test Customer A");
    }

    // Test 2: Check for Existence (Integration Test)
    @Test
    void testExistsByCustomerNumber() {
        // GIVEN: Save a record
        SavingRecord record = new SavingRecord(902, "Test Customer B", 5000.0, 5, "Savings-Deluxe");
        repository.save(record);

        // WHEN
        boolean exists = repository.existsById(902);
        boolean doesNotExist = repository.existsById(999);

        // THEN
        assertThat(exists).isTrue();
        assertThat(doesNotExist).isFalse();
    }
}