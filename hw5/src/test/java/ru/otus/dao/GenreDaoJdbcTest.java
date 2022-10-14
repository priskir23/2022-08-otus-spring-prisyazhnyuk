package ru.otus.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.entities.Genre;
import ru.otus.repo.GenreRepoJdbc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(GenreRepoJdbc.class)
public class GenreDaoJdbcTest {
    private static final long EXISTING_GENRE_ID = 10;
    private static final String EXISTING_GENRE_NAME = "Adventure";
    @Autowired
    private GenreRepoJdbc genreRepoJdbc;

    @Test
    void insertGenre() {
        long id = 50;
        Genre novel = new Genre(id, "Novel");
        genreRepoJdbc.insert(novel);
        Genre inserted = genreRepoJdbc.getById(id);
        assertThat(inserted).usingRecursiveComparison().isEqualTo(novel);
    }

    @Test
    void insertByName() {
        Genre biography = new Genre(null,"Biography");
        long id = genreRepoJdbc.insert(biography);
        Genre biographyInDb = genreRepoJdbc.getById(id);
        assertThat(biographyInDb).hasNoNullFieldsOrProperties();
    }

    @Test
    void insertByIdAndByName() {
        long novelId = 51;
        Genre novel = new Genre(novelId, "Novel");
        genreRepoJdbc.insert(novel);
        Genre inserted = genreRepoJdbc.getById(novelId);
        assertThat(inserted).usingRecursiveComparison().isEqualTo(novel);
        Genre biography = new Genre(null,"Biography");
        long bioId = genreRepoJdbc.insert(biography);
        Genre biographyInDb = genreRepoJdbc.getById(bioId);
        assertThat(biographyInDb).hasNoNullFieldsOrProperties();
    }

    @Test
    void update() {
        Genre biography = new Genre(null,"Biography");
        long bioId = genreRepoJdbc.insert(biography);
        Genre biographyInDb = genreRepoJdbc.getById(bioId);
        assertThat(biographyInDb).hasNoNullFieldsOrProperties();
        assertEquals("Biography", biographyInDb.getName());
        biographyInDb.setName("True Crime");
        genreRepoJdbc.update(biographyInDb);
        Genre grigoryInDb = genreRepoJdbc.getById(bioId);
        assertEquals("True Crime", grigoryInDb.getName());
    }

    @Test
    void getAll() {
        Genre expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        List<Genre> actualPersonList = genreRepoJdbc.getAll();
        assertThat(actualPersonList)
                .containsExactlyInAnyOrder(expectedGenre);
    }

    @Test
    void getById() {
        Genre expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        Genre actualPerson = genreRepoJdbc.getById(expectedGenre.getId());
        assertThat(actualPerson).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void delete() {
        assertThatCode(() -> genreRepoJdbc.getById(EXISTING_GENRE_ID))
                .doesNotThrowAnyException();
        genreRepoJdbc.deleteById(EXISTING_GENRE_ID);
        assertThatThrownBy(() -> genreRepoJdbc.getById(EXISTING_GENRE_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
