package ru.otus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.shell.ConsoleCommandHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SpringBootTest
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TestDBService {
    @Autowired
    ConsoleCommandHandler consoleCommandHandler;

    @MockBean
    OutService outService;

    ByteArrayOutputStream byteArrayOutputStream;
    String UTF = StandardCharsets.UTF_8.name();

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream, true, UTF);
        Mockito.when(outService.getOut()).thenReturn(printStream);
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
        consoleCommandHandler.update(30L, "The Four mushketeers", null, null);
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
        consoleCommandHandler.add("SomeBook", null, Set.of(5L), 10L);
        consoleCommandHandler.showAll(true, true, true);
        assertOutput("""
                The book Book{ id=41, name='SomeBook', genre=Adventure} has been added\r
                ------------------------\r
                Author(id=5, name=Alexandre Dumas)\r
                ------------------------\r
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r
                Book{ id=41, name='SomeBook', genre=Adventure}\r
                ------------------------\r            
                Genre(id=10, name=Adventure)\r
                ------------------------\r
                """);
    }

    @Test
    void testDeleteAllBooks() throws UnsupportedEncodingException {
        consoleCommandHandler.showAll(true, false, false);
        consoleCommandHandler.deleteById(List.of(30L, 40L), Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
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
        consoleCommandHandler.comment(30L, "wow");
        consoleCommandHandler.showAll(true, false, false);
        assertOutput("""
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r
                ------------------------\r
                Book comment with id = 101 has been added\r
                ------------------------\r
                Book{ id=30, name='The Three Mushketeers', genre=Adventure}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure}\r     
                ------------------------\r
                """);
    }

    @Test
    void testShowComment() throws UnsupportedEncodingException {
        consoleCommandHandler.showComments(30L);
        assertOutput("""
                BookComment(id=10, comment=nice)\r
                BookComment(id=20, comment=meh)\r
                BookComment(id=30, comment=boring)\r
                BookComment(id=40, comment=dull)\r
                BookComment(id=50, comment=fine book)\r
                ------------------------\r
                """);
    }

    @Test
    void testShowAuthors() throws UnsupportedEncodingException {
        consoleCommandHandler.showAuthors(30L);
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
