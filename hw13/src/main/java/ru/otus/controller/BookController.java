package ru.otus.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.entities.Book;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @GetMapping("/books")
    public List<BookDto> listPage() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/authors", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAllAuthors() {
        return ResponseEntity.ok(Map.of("authors", authorRepository.findAll()));
    }

    @GetMapping(path = "/genres", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAllGenres() {
        return ResponseEntity.ok(Map.of("genres", genreRepository.findAll()));
    }

    @PostMapping("/book")
    public ResponseEntity<String> createBook(@RequestBody BookDto dto) {
        Book book = BookDto.toDomainObject(dto);
        bookRepository.save(book);
        return ResponseEntity.ok().body("created");
    }

    @PutMapping("/book")
    public ResponseEntity<String> editBook(@RequestBody BookDto dto) {
        Book book = BookDto.toDomainObject(dto);
        bookRepository.save(book);
        return ResponseEntity.ok().body("edited");
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getAllInfoToEditBook(@PathVariable long id) {
        Optional<Book> byId = bookRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(Map.of("book", BookDto.toDto(byId.get())));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
