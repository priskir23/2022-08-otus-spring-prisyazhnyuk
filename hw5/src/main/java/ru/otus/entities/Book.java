package ru.otus.entities;

import lombok.*;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String name;
    private Genre genre;
    private List<Author> authors;

    @Override
    public String toString() {
        return "Book{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + (genre != null ? genre.getName() : "")  +
                ", authors=" + (CollectionUtils.isEmpty(authors) ? "" : authors.stream().map(Author::getName).collect(Collectors.joining(", "))) +
                '}';
    }
}
