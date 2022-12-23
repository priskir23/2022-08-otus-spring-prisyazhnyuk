package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "book")
public class Book {

    @Id
    private String id;
    private String name;
    private List<BookComment> comments = new ArrayList<>();
    private Genre genre;
    private List<Author> authors = new ArrayList<>();

//    private String genreId;
//    private List<String> authorsIds = new ArrayList<>();

    @Override
    public String toString() {
        return "Book{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + (genre != null ? genre.getName() : "") +
                '}';
    }
}
