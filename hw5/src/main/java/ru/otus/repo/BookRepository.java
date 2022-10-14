package ru.otus.repo;

import ru.otus.entities.Book;

import java.util.List;

public interface BookRepository {
    public long insert(Book book);

    public void chainWithGenre(long bookId, long genreId);

    public void chainWithAuthors(long bookId, List<Long> authorIds);

    public Book getById(long id);

    List<Book> getAll();

    public void update(Book book);

    public void deleteById(long id);
}
