package com.example.library.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    @ManyToOne
    private Member member;
    @OneToMany(mappedBy = "loan")
    @ToString.Exclude
    private List<Book> books;
}
