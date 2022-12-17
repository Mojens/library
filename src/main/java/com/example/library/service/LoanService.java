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


    // TODO: 17/12/2022 dobbelt check om denne metode virker korrekt
    public LoanResponse createLoan(@RequestBody LoanRequest loanRequest) {
        Member member = memberRepository.findById(loanRequest.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));
        List<Book> bookList = bookRepository.findAllById(loanRequest.getBookIds());
        if (bookList.size() != loanRequest.getBookIds().size()) {
            throw new RuntimeException("One or more books not found");
        }
        Loan loan = Loan.builder()
                .member(member)
                .checkoutDate(loanRequest.getCheckoutDate())
                .dueDate(loanRequest.getDueDate())
                .returnDate(loanRequest.getReturnDate())
                .build();

        member.setLoans(List.of(loan));
        memberRepository.save(member);

        bookList.forEach(book -> {
            if (book.getLoan() != null) {
                if (!book.getLoan().getDueDate().isAfter(LocalDate.now())) {
                    throw new RuntimeException("\n" + "Book is already loaned with due date: " + book.getLoan().getDueDate() + "\n"
                            + "Please return the book before you can loan it again" + "\n"
                            + "Book title: " + book.getTitle() + "\n");
                }
            }
        });
        loan.setBooks(bookList);
        bookRepository.saveAll(bookList);

        LoanResponse loanResponse = new LoanResponse(loan);
        return loanResponse;
    }


}
