package ru.otus.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.entities.Book;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> allBooks = bookRepository.findAll();
        model.addAttribute("books",
                allBooks.stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList()));
        return "list";
    }

    @GetMapping("/create")
    public String createBook(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("book", new BookDto());
        return "edit";
    }

    @PostMapping({"/create", "/edit"})
    public String createBook(BookDto dto) {
        Book book = BookDto.toDomainObject(dto);
        bookRepository.save(book);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editBook(@RequestParam Long id, Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        Book byId = bookRepository.findById(id).orElseThrow();
        model.addAttribute("book", BookDto.toDto(byId));
        return "edit";
    }

    //без понятия, как вызвать delete метод, через ссылку всегда Get, через <form> всегда вызывается Post, даже несмотря на указание требуемого метода
//    @DeleteMapping("/delete")
    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookRepository.deleteById(id);
        return "redirect:/";
    }

}
