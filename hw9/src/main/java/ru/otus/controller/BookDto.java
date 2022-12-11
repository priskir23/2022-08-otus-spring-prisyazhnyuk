package ru.otus.controller;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {
    private Long id;

    private String name;
    private List<BookComment> comments;
    private Genre genre;
    private Set<Author> authors;

    public String getAllAuthors() {
        return authors != null ? authors.stream().map(Author::getName).collect(Collectors.joining(", ")) : "";
    }

    public String getAllComments() {
        return comments != null ? comments.stream().map(BookComment::getComment).collect(Collectors.joining(",\n")) : "";
    }

    public String getGenreName() {
        return genre != null ? genre.getName() : "";
    }

    public static Book toDomainObject(BookDto dto) {
        return new Book(dto.getId(), dto.getName(), dto.getComments(), dto.getGenre(), dto.getAuthors());
    }

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getName(), book.getComments(), book.getGenre(), book.getAuthors());
    }
}
