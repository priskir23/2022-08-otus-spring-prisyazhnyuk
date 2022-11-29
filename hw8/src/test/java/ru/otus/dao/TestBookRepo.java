package ru.otus.dao;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.CommentRepository;
import ru.otus.repo.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class TestBookRepo {

    private static final int EXPECTED_NUMBER_OF_BOOKS = 2;
    @Autowired
    AuthorRepository authorRepo;
    @Autowired
    BookRepository bookRepo;
    @Autowired
    GenreRepository genreRepo;
    @Autowired
    CommentRepository commentRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        Genre adventure = Genre.builder().id("10").name("Adventure").build();
        genreRepo.save(adventure);
        Author alexandre_dumas = Author.builder().id("5").name("Alexandre Dumas").build();
        authorRepo.save(alexandre_dumas);
        Book the_three_mushketeers = Book.builder().id("30")
                .name("The Three Mushketeers")
                .genre(adventure)
                .authors(List.of(alexandre_dumas))
                .comments(new ArrayList<>()).build();

        Book le_docteur_servan = Book.builder().id("40")
                .name("Le Docteur Servan")
                .authors(List.of(alexandre_dumas))
                .genre(adventure)
                .comments(new ArrayList<>()).build();

        IntStream.range(0, 5).forEach(it -> {
            BookComment comm = BookComment.builder().id(String.valueOf(it)).comment("mush - " + it).build();
            commentRepo.save(comm);
            the_three_mushketeers.getComments().add(comm);
        });
        bookRepo.save(the_three_mushketeers);

        IntStream.range(6, 10).forEach(it -> {
            BookComment comm = BookComment.builder().id(String.valueOf(it)).comment("docteur - " + it).build();
            commentRepo.save(comm);
            le_docteur_servan.getComments().add(comm);
        });
        bookRepo.save(le_docteur_servan);
    }

    @Test
    void testFindOneBook() {
        Optional<Book> bookOptional = bookRepo.findById("30");
        Book book = bookOptional.get();
        Book expectedBook = Book.builder().id("30").name("The Three Mushketeers")
                .genre(new Genre("10", "Adventure"))
                .authors(List.of(new Author("5", "Alexandre Dumas")))
                .comments(List.of(
                        new BookComment("0", "mush - 0"),
                        new BookComment("1", "mush - 1"),
                        new BookComment("2", "mush - 2"),
                        new BookComment("3", "mush - 3"),
                        new BookComment("4", "mush - 4")
                ))
                .build();
        assertThat(expectedBook).usingRecursiveComparison()
                .isEqualTo(book);
    }
    @Test
    void findExpectedBook() {
        val repoBook = bookRepo.findById("30");
        val expectedBook = mongoTemplate.findById("30", Book.class);
        assertThat(expectedBook).usingRecursiveComparison()
                .isEqualTo(repoBook.get());
    }

    @Test
    void shouldReturnCorrectBookListWithAllInfo() {
        val books = bookRepo.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(s -> !s.getName().equals(""))
                .allMatch(s -> s.getGenre() != null);
    }

    @Test
    void testDeleteBook() {
        String id = "30";
        bookRepo.deleteById(id);
        Book byId = mongoTemplate.findById(id, Book.class);
        assertThat(byId).isNull();
    }
}
