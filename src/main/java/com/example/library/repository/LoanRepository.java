package com.example.library.repository;

import com.example.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Transactional
    void deleteAllByBooksIsNull();

}
