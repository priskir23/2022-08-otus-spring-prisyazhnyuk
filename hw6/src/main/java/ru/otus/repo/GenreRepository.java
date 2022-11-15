package ru.otus.repo;

import ru.otus.entities.Genre;

import java.util.List;

public interface GenreRepository {
    Genre save(Genre genre);
    Genre getById(long id);
    List<Genre> getAll();
    void deleteById(long id);
}
