package ru.otus.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.entities.Book;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @GetMapping("/book")
    public List<BookDto> listPage() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());
    }


    @GetMapping(path = "/newBook", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createBook() {
        Map<String, Object> map = new HashMap<>();
        map.put("authors", authorRepository.findAll());
        map.put("genres", genreRepository.findAll());
        map.put("book", new BookDto());
        return ResponseEntity.ok(map);
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
    public ResponseEntity<Map<String, Object>> getAllInfoToEditBook(@PathVariable long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("authors", authorRepository.findAll());
        map.put("genres", genreRepository.findAll());
        Book byId = bookRepository.findById(id).orElseThrow();
        map.put("book", BookDto.toDto(byId));
        return ResponseEntity.ok(map);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
