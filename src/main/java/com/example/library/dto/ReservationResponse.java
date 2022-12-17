package com.example.library.dto;

import com.example.library.entity.Book;
import com.example.library.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {

    private Long reservationId;
    @JsonFormat(pattern = "yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    private LocalDate reservationDate;
    private Long memberId;
    private List<Long> bookIds;

    public ReservationResponse(Reservation r) {
        this.reservationId = r.getId();
        this.reservationDate = r.getReservationDate();
        if (r.getMember() != null) {
            this.memberId = r.getMember().getId();
        }
       if (r.getBooks() != null) {
                this.bookIds = r.getBooks().stream().map(Book::getId).collect(Collectors.toList());
            }
    }
}
