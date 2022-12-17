package com.example.library.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String isbn;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false)
    private String publishYear;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Loan loan;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Reservation reservation;


}
