package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByLoan_DueDateAfter(LocalDate date);

    List<Book> findAllByLoan_DueDateBefore(LocalDate date);
}

