package com.example.library.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class LoanRequest {

    private LocalDate returnDate;
    private LocalDate dueDate;
    private LocalDate checkoutDate;
    private Long memberId;
    private List<Long> bookIds;

}
