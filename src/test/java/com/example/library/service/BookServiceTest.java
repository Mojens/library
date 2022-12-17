package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookServiceTest {

    public BookService bookService;

    @Autowired
    public static BookRepository bookRepository;
    public static LoanRepository loanRepository;
    public static ReservationRepository reservationRepository;

    @BeforeAll
    public static void setupData(@Autowired BookRepository book_Repository) {
        bookRepository = book_Repository;
        bookRepository.deleteAll();

        Book b1 = Book.builder()
                .title("Book1")
                .author("Author1")
                .publisher("Publisher1")
                .publishYear("2020")
                .isbn("1234567890")
                .build();
        Book b2 = Book.builder()
                .title("Book2")
                .author("Author2")
                .publisher("Publisher2")
                .publishYear("2020")
                .isbn("1234567891")
                .build();
        Book b3 = Book.builder()
                .title("Book3")
                .author("Author3")
                .publisher("Publisher3")
                .publishYear("2020")
                .isbn("1234567892")
                .build();
        List<Book> books = List.of(b1, b2, b3);
        bookRepository.saveAll(books);
    }

    @BeforeEach
    public void setBookService(){
        bookService = new BookService(bookRepository, loanRepository, reservationRepository);
    }


    @Test
    void createBook() {
        BookRequest bookRequest = BookRequest.builder()
                .title("Book4")
                .author("Author4")
                .publisher("Publisher4")
                .publishYear("2020")
                .isbn("1234567893")
                .build();
        BookResponse bookResponse = bookService.createBook(bookRequest);
        assertEquals(bookRequest.getTitle(), bookResponse.getTitle());
        Pageable pageable = Pageable.unpaged();
        List<BookResponse> bookResponseList = bookService.readAllBooks(pageable);
        assertNotEquals(0, bookResponseList.size());
        assertEquals(4, bookResponseList.size());
        assertEquals(bookRequest.getTitle(), bookResponseList.get(3).getTitle());



    }

    @Test
    void readAllBooks() {
        Pageable pageable1 = Pageable.unpaged();
        List<BookResponse> bookResponseList1 = bookService.readAllBooks(pageable1);
        assertEquals(3, bookResponseList1.size());


        Pageable pageable2 = Pageable.ofSize(2);
        List<BookResponse> bookResponseList2 = bookService.readAllBooks(pageable2);
        assertEquals(2, bookResponseList2.size());
    }

    @Test
    void updateBook() {
        Book foundBook = bookRepository.findById(1L).orElseThrow(() -> new RuntimeException("book not found"));
        BookRequest bookRequest = BookRequest.builder()
                .title("Book4")
                .author("Author4")
                .publisher("Publisher4")
                .publishYear("2020")
                .isbn("1234567893")
                .build();
        BookResponse bookResponse = bookService.updateBook(bookRequest, foundBook.getId());
        assertEquals(bookRequest.getTitle(), bookResponse.getTitle());
        assertEquals(bookRequest.getAuthor(), bookResponse.getAuthor());
        List<BookResponse> bookResponseList = bookService.readAllBooks(Pageable.unpaged());
        assertEquals(3, bookResponseList.size());
        assertEquals(bookRequest.getTitle(), bookResponseList.get(0).getTitle());

    }

    @Test
    void deleteBook() {
        Book foundBook = bookRepository.findById(1L).orElseThrow(() -> new RuntimeException("book not found"));
        bookService.deleteBook(foundBook.getId());
        List<BookResponse> bookResponseList = bookService.readAllBooks(Pageable.unpaged());
        assertEquals(2, bookResponseList.size());
        assertNotEquals(foundBook.getTitle(), bookResponseList.get(0).getTitle());
    }
}