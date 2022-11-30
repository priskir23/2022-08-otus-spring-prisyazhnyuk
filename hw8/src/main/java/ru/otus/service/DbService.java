package ru.otus.service;

import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;

import java.util.List;
import java.util.Set;

public interface DbService {
    Book addBook(String bookName, String bookId, String genreId, List<String> authorsId);

    List<Author> getAuthorsById(String bookId);

    List<Book> getAllBooks();

    List<Genre> getAllGenres();

    List<Author> getAllAuthors();

    Book updateBook(String bookName, String bookId, String genreId, List<String> authorsId);

    void deleteEntity(List<String> bookIds, List<String> authorIds, List<String> genreIds, List<String> comments);

    BookComment addComment(String bookId, String comment);

    List<BookComment> getComments(String bookId);
}
