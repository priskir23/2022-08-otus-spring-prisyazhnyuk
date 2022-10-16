package ru.otus.service;

import java.util.List;

public interface DbService {
    void addBook(String bookName, Long bookId, Long genreId, List<Long> authorsId);
    void showEntities(boolean showBook, boolean showAuthor, boolean showGenre);
    void updateBook(String bookName, Long bookId, Long genreId, List<Long> authorsId);
    void deleteEntity(List<Long> bookIds, List<Long> authorIds, List<Long> genreIds);
}
