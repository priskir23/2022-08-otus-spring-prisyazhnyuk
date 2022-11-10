package ru.otus.repo;

import ru.otus.entities.Book;

import java.util.List;

public interface BookRepository {
    public Book save(Book book);

    public Book getById(long id);

    List<Book> getAll();

    public void deleteById(long id);
}
