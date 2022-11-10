package ru.otus.service;

import java.util.List;
import java.util.Set;

public interface DbService {
    void addBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId);

    void showEntities(boolean showBook, boolean showAuthor, boolean showGenre);

    void updateBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId);

    void deleteEntity(List<Long> bookIds, List<Long> authorIds, List<Long> genreIds, List<Long> comments);

    void addComment(Long bookId, String comment);
}
