package ru.otus;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private ObjectMapper mapper;

    @Test
    void return2Books() throws Exception {
        List<Book> books = List.of(Book.builder().name("Book1").id(1L).build(),
                Book.builder().name("Book2").id(2L).build());
        given(bookRepository.findAll()).willReturn(books);


        List<BookDto> dtos = books.stream().map(BookDto::toDto).toList();
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtos)));
    }

    @Test
    void getAllAuthors() throws Exception {
        List<Author> authors = List.of(new Author(1L, "Author1"), new Author(2L, "Author2"));
        given(authorRepository.findAll()).willReturn(authors);

        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        gen.setCodec(new ObjectMapper());

        gen.writeStartObject();

        gen.writeObjectField("authors", authors);

        gen.writeEndObject();
        gen.flush();

        mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(writer.toString()));
    }

    @Test
    void getAllGenres() throws Exception {
        List<Genre> genres = List.of(new Genre(1L, "Sci-fi"), new Genre(2L, "Pulp fiction"));
        given(genreRepository.findAll()).willReturn(genres);

        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        gen.setCodec(new ObjectMapper());

        gen.writeStartObject();

        gen.writeObjectField("genres", genres);

        gen.writeEndObject();
        gen.flush();

        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(writer.toString()));
    }

    @Test
    void editBookPut() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        BookDto bookDto = BookDto.toDto(book101);
        bookDto.setName("B00k");
        String js = mapper.writeValueAsString(bookDto);
        mvc.perform(put("/book").contentType(MediaType.APPLICATION_JSON).content(js))
                .andExpect(status().isOk());
        book101.setName("B00k");
        Mockito.verify(bookRepository).save(book101);
    }

    @Test
    void editBookInfo() throws Exception {
        Book.BookBuilder bookBuilder = Book.builder().name("Book101").id(101L);
        Author author = new Author(1L, "Some author");
        Genre genre = new Genre(1L, "Some genre");
        Book book101 = bookBuilder.authors(Set.of(author)).genre(genre).build();

        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));
        given(authorRepository.findAll()).willReturn(List.of(author));
        given(genreRepository.findAll()).willReturn(List.of(genre));

        StringWriter writer = new StringWriter();

        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        gen.setCodec(new ObjectMapper());
        gen.writeStartObject();
        gen.writeObjectField("book", BookDto.toDto(book101));
        gen.writeEndObject();
        gen.flush();

        mvc.perform(get("/book/101"))
                .andExpect(status().isOk())
                .andExpect(content().json(writer.toString()));
    }

    @Test
    void createBookPost() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();

        BookDto bookDto = BookDto.toDto(book101);
        String js = mapper.writeValueAsString(bookDto);
        mvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON).content(js))
                .andExpect(status().isOk());
        Mockito.verify(bookRepository).save(book101);
    }

    @Test
    void deleteBook() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        mvc.perform(delete("/book/101"))
                .andExpect(status().isOk());
        Mockito.verify(bookRepository).deleteById(101L);
    }
}
