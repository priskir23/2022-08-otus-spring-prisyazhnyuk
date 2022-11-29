package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "book")
public class Book {

    @Id
    private String id;

    private String name;

    @DBRef(lazy = true)
    private List<BookComment> comments = new ArrayList<>();

    @DBRef(lazy = true)
    private Genre genre;

    @DBRef(lazy = true)
    private List<Author> authors = new ArrayList<>();


    @Override
    public String toString() {
        return "Book{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + (genre != null ? genre.getName() : "") +
                '}';
    }
}
