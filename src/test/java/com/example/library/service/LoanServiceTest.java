package com.example.library.service;

import com.example.library.dto.LoanRequest;
import com.example.library.dto.LoanResponse;
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

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoanServiceTest {


    public LoanService loanService;
    @Autowired
    public static LoanRepository loanRepository;
    @Autowired
    public static BookRepository bookRepository;
    @Autowired
    public static MemberRepository memberRepository;
    public static ReservationRepository reservationRepository;

    @BeforeAll
    public static void setupData(@Autowired LoanRepository loan_Repository,
                                 @Autowired BookRepository book_Repository,
                                 @Autowired MemberRepository member_Repository,
                                 @Autowired ReservationRepository reservation_Repository) {
        loanRepository = loan_Repository;
        bookRepository = book_Repository;
        memberRepository = member_Repository;
        reservationRepository = reservation_Repository;
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();
        reservationRepository.deleteAll();
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
        Book b4 = Book.builder()
                .title("Book4")
                .author("Author4")
                .publisher("Publisher4")
                .publishYear("2020")
                .isbn("1234567893")
                .build();
        List<Book> books = List.of(b1, b2, b3,b4);
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
    void setupService() {
        loanService = new LoanService(loanRepository, bookRepository, memberRepository);

    }

    @Test
    void getAllLoans() {
        List<LoanResponse> getAllLoans = loanService.getAllLoans();
        assertEquals(3, getAllLoans.size());
    }


    @Test
    void createLoan() {
        LoanRequest loanRequest = LoanRequest.builder()
                .returnDate(LocalDate.now().plusDays(7))
                .dueDate(LocalDate.now().plusDays(14))
                .checkoutDate(LocalDate.now())
                .memberId(2L)
                .bookIds(List.of(4L))
                .build();
        LoanResponse response = loanService.createLoan(loanRequest);
        List<LoanResponse> getAllLoans = loanService.getAllLoans();
        assertEquals(4, getAllLoans.size());
        assertEquals(response.getDueDate(),loanRepository.findById(4L).get().getDueDate());


    }
}