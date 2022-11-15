package ru.otus.repo;

import ru.otus.entities.Author;

import java.util.List;
import java.util.Set;

public interface AuthorRepository {

    Author save(Author author);
    Author getById(long id);
    Set<Author> getByIds(Set<Long> ids);
    List<Author> getAll();
    void deleteById(long id);
}
