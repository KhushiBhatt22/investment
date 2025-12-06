package com.example.investment;

import com.example.investment.model.SavingRecord;
import com.example.investment.repository.SavingRecordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    // Spring will automatically inject the repository instance (Dependency Injection - Requirement 10)
    @Bean
    public CommandLineRunner loadData(SavingRecordRepository repository) {
        return (args) -> {
            System.out.println("Loading initial data...");

            // Data from the exam paper (Page 1)
            // 115 | Jasper Diaz | 150000.0 | 5 | Savings-Deluxe [cite: 20, 21, 22, 23, 24]
            repository.save(new SavingRecord(115, "Jasper Diaz", 150000.0, 5, "Savings-Deluxe"));

            // 112 | Zanip Mendez | 5000.0 | 2 | Savings Deluxe [cite: 28, 29, 30, 31, 32]
            repository.save(new SavingRecord(112, "Zanip Mendez", 5000, 2, "Savings-Deluxe"));

            // 113 | Geronima Esper | 6000.0 | 5 | Savings Regular [cite: 35, 36, 37, 38, 39]
            repository.save(new SavingRecord(113, "Geronima Esper", 6000, 5, "Savings-Regular"));

            // Add an extra record shown in the screenshot on Page 2 (105 | Johnny Jacobi | 7000.0 | 8 | Savings-Regular)
//            repository.save(new SavingRecord(105, "Johnny Jacobi", 7000, 8, "Savings-Regular"));

            System.out.println("Data loaded successfully.");
        };
    }
}