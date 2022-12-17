package com.example.library.dto;

import com.example.library.entity.Book;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publishYear;
    private LoanResponse loan;
    private ReservationResponse reservation;

    public BookResponse(Book b){
        this.id = b.getId();
        this.isbn = b.getIsbn();
        this.title = b.getTitle();
        this.author = b.getAuthor();
        this.publisher = b.getPublisher();
        this.publishYear = b.getPublishYear();
        if(b.getLoan() != null){
            this.loan = new LoanResponse(b.getLoan());
        }
        if(b.getReservation() != null){
            this.reservation = new ReservationResponse(b.getReservation());
        }

    }







}
