package com.example.library.dto;

import com.example.library.entity.Book;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class BookRequest {

    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String publishYear;



    public static Book getBookEntity(BookRequest b){
        return Book.builder()
                .title(b.getTitle())
                .author(b.getAuthor())
                .publisher(b.getPublisher())
                .isbn(b.getIsbn())
                .publishYear(b.getPublishYear())
                .build();
    }


}
