package com.example.library.service;

import com.example.library.dto.LoanRequest;
import com.example.library.dto.LoanResponse;
import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.Member;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {


    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }


    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream().map(LoanResponse::new).toList();
    }

    public LoanResponse createLoan(@RequestBody LoanRequest loanRequest) {
        Member member = memberRepository.findById(loanRequest.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));
        List<Book> bookList = bookRepository.findAllById(loanRequest.getBookIds());
        //Check if the ids is valid books
        if (bookList.size() != loanRequest.getBookIds().size()) {
            throw new RuntimeException("One or more books not found");
        }
        //Check if there is a reservation
        if (bookList.stream().anyMatch(b -> b.getReservation() != null)) {
            //Check if the reservation is active & check if the dueDate from request is after the reservation date
            if (bookList.stream().anyMatch(b -> b.getReservation().getReservationDate().isAfter(LocalDate.now().minusDays(1))) &&
                    bookList.stream().anyMatch(b -> b.getReservation().getReservationDate().isBefore(loanRequest.getDueDate()))) {
                throw new RuntimeException("One or more books are reserved");
            }
        }
        //Check if there is a loan
        if (bookList.stream().anyMatch(b -> b.getLoan() != null)) {
            //Check if the loan is active & check if the dueDate from request is after the loan date
            if (bookList.stream().anyMatch(b -> b.getLoan().getDueDate().isAfter(LocalDate.now()))) {
                throw new RuntimeException("One or more books are already loaned");
            }
        }
        Loan loan = Loan.builder()
                .member(member)
                .books(bookList)
                .checkoutDate(loanRequest.getCheckoutDate())
                .dueDate(loanRequest.getDueDate())
                .returnDate(loanRequest.getReturnDate())
                .build();

        loanRepository.save(loan);
        bookList.forEach(b -> b.setLoan(loan));
        bookRepository.saveAll(bookList);
        return new LoanResponse(loan);
    }


}
