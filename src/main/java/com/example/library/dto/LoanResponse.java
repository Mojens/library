package com.example.library.dto;

import com.example.library.entity.Loan;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanResponse {

    private Long loanId;
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate checkoutDate;
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate dueDate;
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate returnDate;
    private Long memberId;

    public LoanResponse(Loan l) {
        this.loanId = l.getId();
        this.checkoutDate = l.getCheckoutDate();
        this.dueDate = l.getDueDate();
        this.returnDate = l.getReturnDate();
        if (l.getMember() != null) {
            this.memberId = l.getMember().getId();
        }
    }
}
