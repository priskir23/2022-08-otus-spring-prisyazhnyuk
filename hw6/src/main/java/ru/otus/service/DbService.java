package ru.otus.service;

import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DbService {
    Book addBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId);

    Set<Author> getAuthors(Long bookId);

    Map<String, List<?>> showEntities(boolean showBook, boolean showAuthor, boolean showGenre);

    Book updateBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId);

    void deleteEntity(List<Long> bookIds, List<Long> authorIds, List<Long> genreIds, List<Long> comments);

    BookComment addComment(Long bookId, String comment);

    List<BookComment> getComments(Long bookId);
}
