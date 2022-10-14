package ru.otus.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.entities.Book;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.GenreRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DbServiceImpl implements DbService {

    private AuthorRepository authorRepoJdbc;
    private BookRepository bookRepoJdbc;
    private GenreRepository genreRepoJdbc;
    private Displayer displayerImpl;
    @Override
    public void addBook(String bookName, Long bookId, Long genreId, List<Long> authorsId) {
        if (bookName == null || bookName.isEmpty()) {
            displayerImpl.displayMessage("insert name for the book");
        } else {
            Book book = Book.builder()
                    .name(bookName)
                    .id(bookId)
                    .build();
            long inserted = bookRepoJdbc.insert(book);
            if (genreId != null) {
                bookRepoJdbc.chainWithGenre(inserted, genreId);
            }
            if (authorsId != null && !authorsId.isEmpty()) {
                bookRepoJdbc.chainWithAuthors(inserted, authorsId);
            }
            displayerImpl.displayMessage("The book with id = " + inserted + " has been added");
        }
    }

    @Override
    public void showEntities(boolean showBook, boolean showAuthor, boolean showGenre) {
        if (showBook) {
            displayerImpl.displayEntities(bookRepoJdbc.getAll(), "book");
        }
        if (showAuthor) {
            displayerImpl.displayEntities(authorRepoJdbc.getAll(), "author");
        }
        if (showGenre) {
            displayerImpl.displayEntities(genreRepoJdbc.getAll(), "genre");
        }
    }

    @Override
    public void updateBook(String bookName, Long bookId, Long genreId, List<Long> authorsId) {
        //апдейт только по id
        if (bookId == null) {
            displayerImpl.displayMessage("insert id of the book");
        } else {
            Book book = Book.builder()
                    .name(bookName)
                    .id(bookId)
                    .build();
            bookRepoJdbc.update(book);
            if (genreId != null) {
                bookRepoJdbc.chainWithGenre(bookId, genreId);
            }
            if (authorsId != null && !authorsId.isEmpty()) {
                bookRepoJdbc.chainWithAuthors(bookId, authorsId);
            }
            displayerImpl.displayMessage("book with id = " + bookId + " has been updated");
        }
    }

    @Override
    public void deleteEntity(List<Long> bookIds, List<Long> authorIds, List<Long> genreIds) {
        if (genreIds != null) {
            genreIds.forEach(it -> {
                genreRepoJdbc.deleteById(it);
            });
        }
        if (authorIds != null) {
            authorIds.forEach(it -> {
                authorRepoJdbc.deleteById(it);
            });
        }
        if (bookIds != null) {
            bookIds.forEach(it -> {
                bookRepoJdbc.deleteById(it);
            });
        }
        displayerImpl.displayMessage("entities have been deleted");
    }
}
