package com.example.investment.repository;


import com.example.investment.model.SavingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingRecordRepository extends JpaRepository<SavingRecord, Integer> {


}
