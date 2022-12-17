package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.entity.Book;
import com.example.library.entity.Loan;
import com.example.library.entity.Reservation;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {


    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public BookService(BookRepository bookRepository,
                       LoanRepository loanRepository,
                       ReservationRepository reservationRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }


    public BookResponse createBook(@RequestBody BookRequest bookRequest) {
        Book createdBook = BookRequest.getBookEntity(bookRequest);
        return new BookResponse(bookRepository.save(createdBook));
    }

    public List<BookResponse> readAllBooks() {
        return bookRepository.findAll().stream().map(BookResponse::new).toList();
    }

    public BookResponse updateBook(@RequestBody BookRequest bookRequest, @PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found"));
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublisher(bookRequest.getPublisher());
        book.setIsbn(bookRequest.getIsbn());
        book.setPublishYear(bookRequest.getPublishYear());
        return new BookResponse(bookRepository.save(book));
    }

    public BookResponse deleteBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found"));
        BookResponse response = new BookResponse(book);
        bookRepository.delete(book);
        return response;
    }


    public ResponseEntity<String> returnBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found"));
        if (book.getLoan() != null) {
            Loan foundLoan = loanRepository.findById(book.getLoan().getId()).orElseThrow(() -> new RuntimeException("Loan not found"));
            if (foundLoan.getDueDate().isAfter(LocalDate.now())) {
                List<Book> loanBookList;
                loanBookList = foundLoan.getBooks();
                loanBookList.removeIf(b -> b.getId().equals(book.getId()));
                book.setLoan(null);
                bookRepository.save(book);
                foundLoan.setBooks(loanBookList);
                loanRepository.save(foundLoan);
                loanRepository.deleteAllByBooksIsNull();
                return ResponseEntity.ok("Book has been returned late");
            } else {
                List<Book> loanBookList = foundLoan.getBooks();
                loanBookList.removeIf(b -> b.getId().equals(book.getId()));
                book.setLoan(null);
                bookRepository.save(book);
                foundLoan.setBooks(loanBookList);
                loanRepository.save(foundLoan);
                loanRepository.deleteAllByBooksIsNull();
                return ResponseEntity.ok("Book has been removed from Loan");
            }
        }
        if (book.getReservation() != null) {
            Reservation foundReservation = reservationRepository.findById(book.getReservation().getId()).orElseThrow(() -> new RuntimeException("Reservation not found"));
            List<Book> reservationBookList = foundReservation.getBooks();
            reservationBookList.removeIf(b -> b.getId().equals(book.getId()));
            book.setReservation(null);
            bookRepository.save(book);
            foundReservation.setBooks(reservationBookList);
            reservationRepository.save(foundReservation);
            return ResponseEntity.ok("Reservation has been removed");
        }

        return ResponseEntity.ok("Book has been returned");
    }

    public List<BookResponse> allBorrowedBooks() {
        loanRepository.deleteAllByBooksIsNull();
        return bookRepository.findAllByLoan_DueDateAfter(LocalDate.now()).stream().map(BookResponse::new).toList();
    }

    public List<BookResponse> exceededDueDateBooks() {
        loanRepository.deleteAllByBooksIsNull();
        return bookRepository.findAllByLoan_DueDateBefore(LocalDate.now()).stream().map(BookResponse::new).toList();
    }

    public BookResponse findBookById(@PathVariable Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found"));
        return new BookResponse(book);
    }

}
