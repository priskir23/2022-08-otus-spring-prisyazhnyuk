package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repository.AuthorRepository;
import ru.otus.repository.BookRepository;
import ru.otus.repository.GenreRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class);
        createTestData(context);
    }

    private static void createTestData(ConfigurableApplicationContext context) {
        var genreRepository = context.getBean(GenreRepository.class);
        var authorRepository = context.getBean(AuthorRepository.class);
        var bookRepository = context.getBean(BookRepository.class);

        Genre adv = new Genre("1", "Adventure");
        Genre sci = new Genre("2", "Sci-Fi");
        genreRepository.saveAll(Arrays.asList(
                adv,
                sci
        )).subscribe();

        Author push = new Author("1", "Pushkin");
        Author lerm = new Author("2", "Lermontov");
        authorRepository.saveAll(Arrays.asList(
                push,
                lerm,
                new Author("3", "Tolstoy")
        )).subscribe();

        Book book1 = Book.builder()
                .genre(adv)
                .authors(List.of(push)).name("Book1").build();
        Book book2 = Book.builder().genre(sci)
                .authors(List.of(lerm))
                .name("Book2").build();
        bookRepository.saveAll(Arrays.asList(
                book1,
                book2
        )).subscribe();
    }
}