package ru.otus;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.controller.BookController;
import ru.otus.controller.BookDto;
import ru.otus.entities.Book;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void return2Books() throws Exception {
        List<Book> books = List.of(Book.builder().name("Book1").id(1L).build(),
                Book.builder().name("Book2").id(2L).build());
        given(bookRepository.findAll()).willReturn(books);


        List<BookDto> dtos = books.stream().map(BookDto::toDto).toList();
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", dtos));
    }

    @Test
    void editBookShow() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        BookDto bookDto = BookDto.toDto(book101);

        mvc.perform(get("/edit").param("id", "101"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", bookDto));
    }

    @Test
    void editBookPost() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        BookDto bookDto = BookDto.toDto(book101);

        mvc.perform(post("/edit")
                        .requestAttr("id", "101")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "101")
                        .param("name", "bookName"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
        book101.setName("bookName");
        Mockito.verify(bookRepository).save(book101);
    }
}
