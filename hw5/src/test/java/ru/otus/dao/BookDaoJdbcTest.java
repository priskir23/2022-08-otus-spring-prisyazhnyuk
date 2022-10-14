package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;
import ru.otus.repo.AuthorRepoJdbc;
import ru.otus.repo.BookRepoJdbc;
import ru.otus.repo.GenreRepoJdbc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@Import({BookRepoJdbc.class, AuthorRepoJdbc.class, GenreRepoJdbc.class})
public class BookDaoJdbcTest {
    @Autowired
    private BookRepoJdbc bookRepoJdbc;
    @Autowired
    private GenreRepoJdbc genreRepoJdbc;

    @Autowired
    private AuthorRepoJdbc authorRepoJdbc;

    @Test
    void getById() {
        long id = 30L;
        Book musketeers = bookRepoJdbc.getById(id);
        assertNotNull(musketeers);
        assertEquals("The Three Mushketeers", musketeers.getName());
        assertEquals("Adventure", musketeers.getGenre().getName());
        assertEquals(1, musketeers.getAuthors().size());
        assertEquals("Alexandre Dumas", musketeers.getAuthors().get(0).getName());
    }

    @Test
    void getAll() {
        List<Book> allBooks = bookRepoJdbc.getAll();
        assertEquals(2, allBooks.size());
    }

    @Test
    void insertById() {
        long id = 121;
        Book book = Book.builder()
                .name("Teach Yourself C++ In 21 Days")
                .id(id)
                .build();
        bookRepoJdbc.insert(book);
        Book byId = bookRepoJdbc.getById(id);
        assertThat(byId).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    void insertByName() {
        Book book = Book.builder()
                .name("Teach Yourself C++ In 21 Days")
                .build();
        long id = bookRepoJdbc.insert(book);
        Book bookInDb = bookRepoJdbc.getById(id);
        assertThat(bookInDb).usingRecursiveComparison()
                .withEqualsForFields((String a, String b) -> a.equals(b), "name")
                .isEqualTo(bookInDb);
    }

    @Test
    void chainWithGenre() {
        Genre biography = new Genre(null,"Fantasy");
        long genreId = genreRepoJdbc.insert(biography);
        Book book = Book.builder()
                .name("Teach Yourself C++ In 21 Days")
                .build();
        long bookId = bookRepoJdbc.insert(book);
        bookRepoJdbc.chainWithGenre(bookId, genreId);
        Book byId = bookRepoJdbc.getById(bookId);
        assertNotNull(byId);
        assertEquals("Teach Yourself C++ In 21 Days", byId.getName());
        assertNotNull(byId.getGenre());
        assertEquals("Fantasy", byId.getGenre().getName());
    }

    @Test
    void chainWithAuthor() {
        Author jesse = new Author(null,"Jesse Liberty");
        long jesseId = authorRepoJdbc.insert(jesse);
        Author bradley = new Author(null,"Bradley L. Jones");
        long bradleyId = authorRepoJdbc.insert(bradley);
        Book book = Book.builder()
                .name("Teach Yourself C++ In 21 Days")
                .build();
        long bookId = bookRepoJdbc.insert(book);
        bookRepoJdbc.chainWithAuthors(bookId, Arrays.asList(jesseId, bradleyId));
        Book byId = bookRepoJdbc.getById(bookId);
        assertNotNull(byId);
        assertEquals("Teach Yourself C++ In 21 Days", byId.getName());
        assertNotNull(byId.getAuthors());
        assertEquals(2, byId.getAuthors().size());
    }

    @Test
    void update() {
        long id = 121;
        Book book = Book.builder()
                .name("Teach Yourself C++ In 21 Days")
                .id(id)
                .build();
        bookRepoJdbc.insert(book);
        Book byId = bookRepoJdbc.getById(id);
        assertThat(byId).usingRecursiveComparison().isEqualTo(book);
        byId.setName("C++ Programming Language");
        bookRepoJdbc.update(byId);
        Book updated = bookRepoJdbc.getById(id);
        assertEquals("C++ Programming Language", updated.getName());
    }

    @Test
    void delete() {
        //под 30 Мушкетеры
        long mushId = 30L;
        assertThatCode(() -> bookRepoJdbc.getById(mushId))
                .doesNotThrowAnyException();
        bookRepoJdbc.deleteById(mushId);
        assertThatThrownBy(() -> bookRepoJdbc.getById(mushId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
