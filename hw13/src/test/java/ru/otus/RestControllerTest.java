package ru.otus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.controller.BookController;
import ru.otus.controller.BookDto;
import ru.otus.entities.Book;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class RestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private ObjectMapper mapper;


    @WithMockUser(
            username = "hello",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testGetAllBooksForUser() throws Exception {
        List<Book> books = List.of(Book.builder().name("Book1").id(1L).build(),
                Book.builder().name("Book2").id(2L).build());
        given(bookRepository.findAll()).willReturn(books);

        List<BookDto> dtos = books.stream().map(BookDto::toDto).toList();
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtos)));
    }

    @WithMockUser(
            username = "hello",
            authorities = {"ROLE_USER"}
    )
    @Test
    public void testPutAnyBookForUserForbidden() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        BookDto bookDto = BookDto.toDto(book101);
        bookDto.setName("B00k");
        String js = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put("/book")
                //с данной вещью тест будет возвращать 200
//                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(js))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "hello",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void testPutAnyBookForAdminAllowed() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        BookDto bookDto = BookDto.toDto(book101);
        bookDto.setName("B00k");
        String js = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put("/book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(js)
                        )
                .andExpect(status().isOk());
        book101.setName("B00k");
        Mockito.verify(bookRepository).save(book101);
    }

    @WithMockUser(
            username = "hello",
            authorities = {"ROLE_USER"}
    )
    @Test
    void deleteBook() throws Exception {
        Book book101 = Book.builder().name("Book101").id(101L).build();
        given(bookRepository.findById(101L)).willReturn(Optional.of(book101));

        mockMvc.perform(delete("/book/101").with(csrf()))
                .andExpect(authenticated())
                .andExpect(status().isOk());
        Mockito.verify(bookRepository).deleteById(101L);
    }

}
