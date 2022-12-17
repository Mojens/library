package com.example.library.api;

import com.example.library.dto.*;
import com.example.library.service.BookService;
import com.example.library.service.LoanService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;

    public BookController(BookService bookService, LoanService loanService) {
        this.bookService = bookService;
        this.loanService = loanService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookResponse createBook(@RequestBody BookRequest bookRequest) {
        return bookService.createBook(bookRequest);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BookResponse updateBook(@RequestBody BookRequest bookRequest, @PathVariable Long id) {
        return bookService.updateBook(bookRequest, id);
    }

    @GetMapping
    List<BookResponse> readAllBooks() {
        return bookService.readAllBooks();
    }

    @DeleteMapping(value = "/{id}")
    public BookResponse deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @PostMapping(value = "/return/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        return bookService.returnBook(id);
    }

    @GetMapping(value = "/exceeded")
    public List<BookResponse> getExceededBooks() {
        return bookService.exceededDueDateBooks();
    }

    @GetMapping(value = "/borrowed")
    public List<BookResponse> getBorrowedBooks() {
        return bookService.allBorrowedBooks();
    }

    @PostMapping(value = "/loan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoanResponse createLoan(@RequestBody LoanRequest loanRequest) {
        return loanService.createLoan(loanRequest);
    }

    @GetMapping(value = "/loans")
    public List<LoanResponse> readAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping(value = "/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @GetMapping(value = "/members")
    public List<MemberResponse> getAllMembers() {
        return loanService.getAllMembers();
    }
    @GetMapping(value = "/members/{id}")
    public MemberResponse getMemberById(@PathVariable Long id) {
        return loanService.getMemberById(id);
    }


}
