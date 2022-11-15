package ru.otus.dao;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;
import ru.otus.repo.BookRepoJpa;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookRepoJpa.class)
public class TestBookRepo {

    private static final int EXPECTED_NUMBER_OF_BOOKS = 2;
    private static final int EXPECTED_QUERIES_COUNT_FOR_GENRE = 1;
    @Autowired
    private BookRepoJpa bookRepoJpa;

    @Autowired
    private TestEntityManager em;

    @AfterEach
    void tearDown() {
        SessionFactory unwrap = em.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        unwrap.getStatistics().clear();
    }

    @Test
    void testFindOneBook() {
        Book book = bookRepoJpa.getById(30);
        Book expectedBook = Book.builder().id(30L).name("The Three Mushketeers")
                .genre(new Genre(10L, "Adventure"))
                .authors(Set.of(new Author(5L, "Alexandre Dumas")))
                .comments(List.of(
                        new BookComment(10L, "nice"),
                        new BookComment(20L, "meh"),
                        new BookComment(30L, "boring"),
                        new BookComment(40L, "dull"),
                        new BookComment(50L, "fine book")
                ))
                .build();
        assertThat(expectedBook).usingRecursiveComparison()
                .isEqualTo(book);
    }

    @Test
    void findExpectedBook() {
        val repoBook = bookRepoJpa.getById(30);
        val expectedBook = em.find(Book.class, 30L);
        assertThat(expectedBook).usingRecursiveComparison()
                .isEqualTo(repoBook);
    }

    @Test
    void shouldReturnCorrectBookListWithAllInfo() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        System.out.println("\n\n\n\n----------------------------------------------------------------------------------------------------------");
        val books = bookRepoJpa.getAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(s -> !s.getName().equals(""))
                .allMatch(s -> s.getGenre() != null);
        System.out.println("----------------------------------------------------------------------------------------------------------\n\n\n\n");
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT_FOR_GENRE);
    }

    @Test
    void testDeleteBook() {
        long id = 30L;
        bookRepoJpa.deleteById(id);
        Book byId = bookRepoJpa.getById(30);
        assertThat(byId).isNull();
    }
}
