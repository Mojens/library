package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.Member;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.MemberRepository;
import com.example.library.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookServiceTest {

    public BookService bookService;

    @Autowired
    public static BookRepository bookRepository;
    @Autowired
    public static LoanRepository loanRepository;
    @Autowired
    public static ReservationRepository reservationRepository;
    public static MemberRepository memberRepository;

    @BeforeAll
    public static void setupData(@Autowired BookRepository book_Repository,
                                 @Autowired LoanRepository loan_Repository,
                                 @Autowired ReservationRepository reservation_Repository,
                                 @Autowired MemberRepository member_Repository) {
        bookRepository = book_Repository;
        loanRepository = loan_Repository;
        reservationRepository = reservation_Repository;
        memberRepository = member_Repository;
        Member m1 = Member.builder()
                .userName("user1")
                .password("password1")
                .eMail("user1@user.com")
                .build();
        Member m2 = Member.builder()
                .userName("user2")
                .password("password2")
                .eMail("user2@user.dk")
                .build();
        Member m3 = Member.builder()
                .userName("user3")
                .password("password3")
                .eMail("user3@user.dk")
                .build();
        List<Member> members = List.of(m1, m2, m3);
        memberRepository.saveAll(members);

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

        Loan loan1 = Loan.builder()
                .member(m1)
                .returnDate(LocalDate.now().plusDays(7))
                .dueDate(LocalDate.now().minusDays(14))
                .checkoutDate(LocalDate.now())
                .books(List.of(b1))
                .build();
        loanRepository.save(loan1);
        m1.setLoans(List.of(loan1));
        memberRepository.save(m1);
        b1.setLoan(loan1);
        bookRepository.saveAll(List.of(b1));

        Loan loan2 = Loan.builder()
                .member(m2)
                .returnDate(LocalDate.now().plusDays(7))
                .dueDate(LocalDate.now().plusDays(14))
                .checkoutDate(LocalDate.now())
                .books(List.of(b2))
                .build();
        loanRepository.save(loan2);
        m2.setLoans(List.of(loan2));
        memberRepository.save(m2);
        b2.setLoan(loan2);
        bookRepository.saveAll(List.of(b2));

        Loan loan3 = Loan.builder()
                .member(m3)
                .returnDate(LocalDate.now().plusDays(7))
                .dueDate(LocalDate.now().plusDays(14))
                .checkoutDate(LocalDate.now())
                .books(List.of(b3))
                .build();
        loanRepository.save(loan3);
        m3.setLoans(List.of(loan3));
        memberRepository.save(m3);
        b3.setLoan(loan3);
        bookRepository.saveAll(List.of(b3));
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

    @Test
    void allBorrowedBooks(){
        List<BookResponse> bookResponseList = bookService.allBorrowedBooks();
        assertEquals(2, bookResponseList.size());
    }

    @Test
    void exceededDueDateBooks(){
        List<BookResponse> bookResponseList = bookService.exceededDueDateBooks();
        assertEquals(1, bookResponseList.size());
    }

    @Test
    void returnBook(){
        Book foundBook = bookRepository.findById(3L).orElseThrow(() -> new RuntimeException("book not found"));
        bookService.returnBook(foundBook.getId());
        List<BookResponse> bookResponseList = bookService.allBorrowedBooks();
        assertEquals(1, bookResponseList.size());
        List<Loan> loanList = loanRepository.findAll();
        assertEquals(2, loanList.size());
    }




}