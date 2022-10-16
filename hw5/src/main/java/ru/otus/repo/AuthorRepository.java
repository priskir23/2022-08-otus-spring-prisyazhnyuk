package ru.otus.repo;

import ru.otus.entities.Author;

import java.util.List;

public interface AuthorRepository {
    long insert(Author author);
    void update(Author author);
    Author getById(long id);
    List<Author> getAll();
    void deleteById(long id);
}
