package ru.otus.repo;

import ru.otus.entities.Genre;

import java.util.List;

public interface GenreRepository {
    long insert(Genre genre);
    void update(Genre genre);
    Genre getById(long id);
    List<Genre> getAll();
    void deleteById(long id);
}
