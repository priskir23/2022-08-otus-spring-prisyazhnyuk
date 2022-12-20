package ru.otus.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repository.BookRepository;
import ru.otus.utils.BookDto;

@SpringBootTest
public class BookControllerTest {

    @Autowired
    private RouterFunction<ServerResponse> bookRoute;

    @Autowired
    private RouterFunction<ServerResponse> authorRoute;

    @Autowired
    private RouterFunction<ServerResponse> genreRoute;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void testPostBook() {
        Book book = Book.builder().id("1").name("Some name").build();
        BookDto bookDto = BookDto.toDto(book);
        //для того, чтобы не ругался mono
        Mockito.when(bookRepository.save(book)).thenReturn(Mono.just(book));
        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoute)
                .build();
        client.post()
                .uri("/book")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookDto), BookDto.class)
                .exchange()
                .expectStatus()
                .isOk();
        Mockito.verify(bookRepository).save(book);
    }

    @Test
    void testDeleteBook() {
        Book book = Book.builder().id("1").name("Some name").build();
        BookDto bookDto = BookDto.toDto(book);
        Mockito.when(bookRepository.deleteById("1"))
                //никак по-другому не сделать Mono<void>
                .thenReturn(Mono.just("").then());
        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoute)
                .build();
        client.delete()
                .uri("/book/1")
                .exchange()
                .expectStatus()
                .isOk();
        Mockito.verify(bookRepository).deleteById("1");
    }

    @Test
    void testGetBookByIdRoute() {
        Book book = Book.builder().id("1").name("Some name").build();
        Mockito.when(bookRepository.findById("1")).thenReturn(Mono.just(book));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoute)
                .build();
        client.get()
                .uri("/book/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class);
    }

    @Test
    void testBooksRoute() {

        Book book1 = Book.builder().id("1").name("Some name1").build();
        Book book2 = Book.builder().id("2").name("Some name2").build();

        //для того, чтобы не ругался mono
        Mockito.when(bookRepository.findAll()).thenReturn(Flux.just(book1, book2));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRoute)
                .build();

        client.get()
                .uri("/books")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(BookDto.toDto(book2));
    }

    @Test
    void testAuthorsRoute() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRoute)
                .build();

        client.get()
                .uri("/authors")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Author.class);
    }

    @Test
    void testGenresRoute() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRoute)
                .build();

        client.get()
                .uri("/genres")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Genre.class);
    }

}
