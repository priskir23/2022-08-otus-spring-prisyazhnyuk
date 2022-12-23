package ru.otus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.entities.Author;
import ru.otus.entities.Genre;
import ru.otus.repository.AuthorRepository;
import ru.otus.repository.BookRepository;
import ru.otus.repository.GenreRepository;
import ru.otus.utils.BookDto;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class FunctionalEndpointConfig {

    public RouterFunction<ServerResponse> pageRoute(@Value("classpath:/static/index.html") final Resource html) {
        return route()
                .GET("/", request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(html))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookRoute(BookRepository bookRepository) {
        return route()
                .POST("/book", accept(MediaType.APPLICATION_JSON),
                        createOrUpdateBook(bookRepository)
                )
                .PUT("/book", accept(MediaType.APPLICATION_JSON),
                        createOrUpdateBook(bookRepository)
                )
                .GET("/books", request -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(bookRepository.findAll().map(BookDto::toDto), BookDto.class))
                .GET("/book/{id}", request -> bookRepository
                        .findById(request.pathVariable("id"))
                                .flatMap(
                                        book -> ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(BookDto.toDto(book))
                                )
                        .switchIfEmpty(notFound().build())
                )
                .DELETE("/book/{id}", request -> bookRepository
                        .deleteById(request.pathVariable("id"))
                        .then(
                                ok()
                                        .build()
                        )
                )
                .build();

    }

    private static HandlerFunction<ServerResponse> createOrUpdateBook(BookRepository bookRepository) {
        return request -> request.bodyToMono(BookDto.class)
                .map(BookDto::toDomainObject)
                .doOnNext(book -> bookRepository.save(book).subscribe())
                .then(
                        ok()
                        .build()
                );
    }

    @Bean
    public RouterFunction<ServerResponse> authorRoute(AuthorRepository authorRepository) {
        return route()
                .GET("/authors", request -> ok().
                        contentType(MediaType.APPLICATION_JSON)
                        .body(authorRepository.findAll(), Author.class)
                        .switchIfEmpty(
                                notFound().build()
                        )
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> genreRoute(GenreRepository genreRepository) {
        return route()
                .GET("/genres", request -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(genreRepository.findAll(), Genre.class)
                        .switchIfEmpty(
                                notFound().build()
                        )
                )
                .build();
    }
}
