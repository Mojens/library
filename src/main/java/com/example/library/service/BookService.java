package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.entity.Book;
import com.example.library.repository.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BookService {


    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public BookResponse createBook(@RequestBody BookRequest bookRequest) {
        Book createdBook = BookRequest.getBookEntity(bookRequest);
        return new BookResponse(bookRepository.save(createdBook));
    }

    public List<BookResponse> readAllBooks(Pageable p){
        return bookRepository.findAll(p).stream().map(BookResponse::new).toList();
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







}
