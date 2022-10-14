package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.entities.Author;
import ru.otus.repo.AuthorRepoJdbc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(AuthorRepoJdbc.class)
public class AuthorDaoJdbcTest {
    private static final long EXISTING_AUTHOR_ID = 5;
    private static final String EXISTING_AUTHOR_NAME = "Alexandre Dumas";
    @Autowired
    private AuthorRepoJdbc authorRepoJdbc;

    @Test
    void insertAuthor() {
        long id = 50;
        Author ivanov = new Author(id, "Ivan Ivanov");
        authorRepoJdbc.insert(ivanov);
        Author inserted = authorRepoJdbc.getById(id);
        assertThat(inserted).usingRecursiveComparison().isEqualTo(ivanov);
    }

    @Test
    void insertByName() {
        Author vasily = new Author(null,"Vasily Vasilyev");
        long id = authorRepoJdbc.insert(vasily);
        Author vasilyInDb = authorRepoJdbc.getById(id);
        assertThat(vasilyInDb).hasNoNullFieldsOrProperties();
    }

    @Test
    void insertByIdAndByName() {
        long ivanId = 51;
        Author ivanov = new Author(ivanId, "Ivan Ivanov");
        authorRepoJdbc.insert(ivanov);
        Author inserted = authorRepoJdbc.getById(ivanId);
        assertThat(inserted).usingRecursiveComparison().isEqualTo(ivanov);
        Author vasily = new Author(null,"Vasily Vasilyev");
        long vasilyId = authorRepoJdbc.insert(vasily);
        Author vasilyInDb = authorRepoJdbc.getById(vasilyId);
        assertThat(vasilyInDb).hasNoNullFieldsOrProperties();
    }

    @Test
    void update() {
        Author vasily = new Author(null,"Vasily Vasilyev");
        long vasilyId = authorRepoJdbc.insert(vasily);
        Author vasilyInDb = authorRepoJdbc.getById(vasilyId);
        assertThat(vasilyInDb).hasNoNullFieldsOrProperties();
        assertEquals("Vasily Vasilyev", vasilyInDb.getName());
        vasilyInDb.setName("Grigory Grigoryev");
        authorRepoJdbc.update(vasilyInDb);
        Author grigoryInDb = authorRepoJdbc.getById(vasilyId);
        assertEquals("Grigory Grigoryev", grigoryInDb.getName());
    }

    @Test
    void getAll() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        List<Author> actualPersonList = authorRepoJdbc.getAll();
        assertThat(actualPersonList)
                .containsExactlyInAnyOrder(expectedAuthor);
    }

    @Test
    void getById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_ID, EXISTING_AUTHOR_NAME);
        Author actualPerson = authorRepoJdbc.getById(expectedAuthor.getId());
        assertThat(actualPerson).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void delete() {
        assertThatCode(() -> authorRepoJdbc.getById(EXISTING_AUTHOR_ID))
                .doesNotThrowAnyException();
        authorRepoJdbc.deleteById(EXISTING_AUTHOR_ID);
        assertThatThrownBy(() -> authorRepoJdbc.getById(EXISTING_AUTHOR_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
