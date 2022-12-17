package com.example.library.configuration;

import com.example.library.dto.LoanRequest;
import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.Member;
import com.example.library.entity.Reservation;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.MemberRepository;
import com.example.library.repository.ReservationRepository;
import com.example.library.service.BookService;
import com.example.library.service.LoanService;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class setupLibrary implements ApplicationRunner {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final LoanService loanService;

    private final BookService bookService;

    public setupLibrary(BookRepository bookRepository,
                        LoanRepository loanRepository,
                        MemberRepository memberRepository,
                        ReservationRepository reservationRepository,
                        LoanService loanService,
                        BookService bookService) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.loanService = loanService;
        this.bookService = bookService;
    }

    @Override
    @SneakyThrows
    public void run(ApplicationArguments args) {
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
        List<Book> books = List.of(b1, b2, b3, b4);
        bookRepository.saveAll(books);

        //Make a loan
        Loan loan1 = Loan.builder()
                .member(m1)
                .returnDate(LocalDate.now().plusDays(7))
                .dueDate(LocalDate.now().plusDays(14))
                .checkoutDate(LocalDate.now())
                .books(List.of(b1))
                .build();
        loanRepository.save(loan1);
        m1.setLoans(List.of(loan1));
        memberRepository.save(m1);
        b1.setLoan(loan1);
        bookRepository.saveAll(List.of(b1));


        //Make a reservation
        Reservation reservation1 = Reservation.builder()
                .member(m2)
                .reservationDate(LocalDate.now())
                .books(List.of(b3))
                .build();
        reservationRepository.save(reservation1);
        m2.setReservations(List.of(reservation1));
        memberRepository.save(m2);
        b3.setReservation(reservation1);
        bookRepository.save(b3);


        //Test Service Method

        LoanRequest loanRequest = LoanRequest.builder()
                .returnDate(LocalDate.now().plusDays(7))
                .dueDate(LocalDate.now().plusDays(14))
                .checkoutDate(LocalDate.now())
                .memberId(m2.getId())
                .bookIds(List.of(b4.getId()))
                .build();
        loanService.createLoan(loanRequest);

        System.out.println(bookService.returnBook(b1.getId()));
        /*
        if (b1.getLoan() != null) {
            Loan foundLoan = loanRepository.findById(b1.getLoan().getId()).get();
            List<Book> loanBookList = foundLoan.getBooks();
            loanBookList.removeIf(book -> book.getId().equals(b1.getId()));
            b1.setLoan(null);
            bookRepository.save(b1);
            foundLoan.setBooks(loanBookList);
            loanRepository.save(foundLoan);
            System.out.println(loanRepository.findAll());
            loanRepository.deleteAllByBooksIsNull();
            System.out.println(loanRepository.findAll());
        }
         */


    }
}
