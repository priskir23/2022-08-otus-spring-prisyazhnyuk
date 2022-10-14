package ru.otus.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TestDBService {
    @Autowired
    DbService dbService;

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
        dbService.updateBook("woop", null, null, null);
        String insertOutput = byteArrayOutputStream.toString(UTF);
        assertOutput("""
                insert id of the book\r
                ------------------------\r
                """);
    }

    @Test
    void testUpdateSomeExistingBook() throws UnsupportedEncodingException {
        dbService.updateBook("The Four mushketeers", 30L, null, null);
        dbService.showEntities(true, false, false);

        assertOutput("""
                book with id = 30 has been updated\r
                ------------------------\r
                Book{ id=30, name='The Four mushketeers', genre=Adventure, authors=Alexandre Dumas}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure, authors=Alexandre Dumas}\r
                ------------------------\r
                """);
    }

    @Test
    void testInsertBook() throws UnsupportedEncodingException {
        dbService.addBook("SomeBook", null, 10L, List.of(5L));
        dbService.showEntities(true, true, true);
        assertOutput("""
                The book with id = 41 has been added\r
                ------------------------\r
                Book{ id=30, name='The Three Mushketeers', genre=Adventure, authors=Alexandre Dumas}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure, authors=Alexandre Dumas}\r
                Book{ id=41, name='SomeBook', genre=Adventure, authors=Alexandre Dumas}\r
                ------------------------\r
                Author: id=5, name='Alexandre Dumas'\r
                ------------------------\r
                Genre: id=10, name='Adventure'\r
                ------------------------\r
                """);
    }

    @Test
    void testDeleteAllBooks() throws UnsupportedEncodingException {
        dbService.showEntities(true, false, false);
        dbService.deleteEntity(List.of(30L, 40L), null, null);
        dbService.showEntities(true, false, false);
        assertOutput("""
                Book{ id=30, name='The Three Mushketeers', genre=Adventure, authors=Alexandre Dumas}\r
                Book{ id=40, name='Le Docteur Servan', genre=Adventure, authors=Alexandre Dumas}\r
                ------------------------\r
                entities have been deleted\r
                ------------------------\r
                there is no book to show\r
                """);
    }

    private void assertOutput(String expected) throws UnsupportedEncodingException {
        String output = byteArrayOutputStream.toString(UTF);
        System.out.println(output);
        Assertions.assertEquals(expected, output);
    }
}
