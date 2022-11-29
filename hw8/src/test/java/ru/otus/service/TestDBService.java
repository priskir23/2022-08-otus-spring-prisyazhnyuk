package ru.otus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.CommentRepository;
import ru.otus.repo.GenreRepository;
import ru.otus.shell.ConsoleCommandHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

@AutoConfigureDataMongo
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDBService {
    @Autowired
    ConsoleCommandHandler consoleCommandHandler;
    @Autowired
    AuthorRepository authorRepo;
    @Autowired
    BookRepository bookRepo;
    @Autowired
    GenreRepository genreRepo;
    @Autowired
    CommentRepository commentRepo;

    @MockBean
    OutService outService;

    ByteArrayOutputStream byteArrayOutputStream;
    String UTF = StandardCharsets.UTF_8.name();

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream, true, UTF);
        Mockito.when(outService.getOut()).thenReturn(printStream);

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
    void testUpdateWithEmptyId() throws UnsupportedEncodingException {
        consoleCommandHandler.update(null, "woop", null, null);
        String insertOutput = byteArrayOutputStream.toString(UTF);
        assertOutput("""
                insert id of the book\r
                ------------------------\r
                """);
    }

    @Test
    void testUpdateSomeExistingBook() throws UnsupportedEncodingException {
        consoleCommandHandler.update("30", "The Four mushketeers", null, null);
        consoleCommandHandler.showAll(true, false, false);

        assertOutput("""
                The book with id = 30 has been updated\r
                ------------------------\r
                Book{ id=30, name='The Four mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r
                ------------------------\r
                """);
    }

    @Test
    void testInsertBook() throws UnsupportedEncodingException {
        consoleCommandHandler.add("SomeBook", null, List.of("5"), "10");
        consoleCommandHandler.showAll(true, true, true);
        Optional<Book> someBook = bookRepo.findByName("SomeBook");
        assertOutput("""
                The book Book{ id=bookID, name='SomeBook', genre=Adventure} has been added\r
                ------------------------\r
                Author(id=5, name=Alexandre Dumas)\r
                ------------------------\r
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r
                Book{ id=bookID, name='SomeBook', genre=Adventure}\r
                ------------------------\r            
                Genre(id=10, name=Adventure)\r
                ------------------------\r
                """.replace("bookID", someBook.get().getId()));
    }

    @Test
    void testDeleteAllBooks() throws UnsupportedEncodingException {
        consoleCommandHandler.showAll(true, false, false);
        consoleCommandHandler.deleteById(List.of("30", "40"), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        consoleCommandHandler.showAll(true, false, false);
        assertOutput("""
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r
                ------------------------\r
                entities have been deleted\r
                ------------------------\r
                there is no book to show\r
                """);
    }

    @Test
    void testInsertComment() throws UnsupportedEncodingException {
        consoleCommandHandler.showAll(true, false, false);
        consoleCommandHandler.comment("30", "wow");
        consoleCommandHandler.showAll(true, false, false);

        Optional<BookComment> wow = commentRepo.findByComment("wow");
        BookComment bookComment = wow.get();
        String commId = bookComment.getId();
        assertOutput("""
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r
                ------------------------\r
                Book comment with id = commId has been added\r
                ------------------------\r
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r     
                ------------------------\r
                """.replace("commId", commId));
    }

    @Test
    void testShowComment() throws UnsupportedEncodingException {
        consoleCommandHandler.showComments("30");
        assertOutput("""
                BookComment(id=0, comment=mush - 0)\r
                BookComment(id=1, comment=mush - 1)\r
                BookComment(id=2, comment=mush - 2)\r
                BookComment(id=3, comment=mush - 3)\r
                BookComment(id=4, comment=mush - 4)\r
                ------------------------\r
                """);
    }

    @Test
    void testShowAuthors() throws UnsupportedEncodingException {
        consoleCommandHandler.showAuthors("30");
        assertOutput("""
                Author(id=5, name=Alexandre Dumas)\r
                ------------------------\r
                """);
    }

    private void assertOutput(String expected) throws UnsupportedEncodingException {
        String output = byteArrayOutputStream.toString(UTF);
        System.out.println(output);
        Assertions.assertEquals(expected, output);
    }
}
