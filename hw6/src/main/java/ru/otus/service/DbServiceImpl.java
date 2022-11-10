package ru.otus.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.CommentRepository;
import ru.otus.repo.GenreRepository;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DbServiceImpl implements DbService {

    private AuthorRepository authorRepoJpa;
    private BookRepository bookRepoJpa;
    private GenreRepository genreRepoJpa;
    private CommentRepository commentRepoJpa;
    private Displayer displayerImpl;

    @Override
    public void addBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId) {
        if (bookName == null || bookName.isEmpty()) {
            displayerImpl.displayMessage("insert name for the book");
        } else {
            Book.BookBuilder bookBuilder = Book.builder()
                    .name(bookName)
                    .id(bookId);

            if (genreId != null) {
                Genre genre = genreRepoJpa.getById(genreId);
                bookBuilder = bookBuilder.genre(genre);
            }
            if (authorsId != null && !authorsId.isEmpty()) {
                Set<Author> authors = authorRepoJpa.getByIds(authorsId);
                bookBuilder = bookBuilder.authors(authors);
            }
            Book inserted = bookRepoJpa.save(bookBuilder.build());
            displayerImpl.displayMessage("The book " + inserted + " has been added");
        }
    }

    @Override
    public void showEntities(boolean showBook, boolean showAuthor, boolean showGenre) {
        if (showBook) {
            displayerImpl.displayEntities(bookRepoJpa.getAll(), "book");
        }
        if (showAuthor) {
            displayerImpl.displayEntities(authorRepoJpa.getAll(), "author");
        }
        if (showGenre) {
            displayerImpl.displayEntities(genreRepoJpa.getAll(), "genre");
        }
    }

    @Override
    public void updateBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId) {
        //апдейт только по id
        if (bookId == null) {
            displayerImpl.displayMessage("insert id of the book");
        } else {
            Book book = bookRepoJpa.getById(bookId);
            book.setName(bookName);

            if (genreId != null) {
                Genre genre = genreRepoJpa.getById(genreId);
                book.setGenre(genre);
            }
            if (authorsId != null && !authorsId.isEmpty()) {
                Set<Author> authors = authorRepoJpa.getByIds(authorsId);
                book.setAuthors(authors);
            }
            bookRepoJpa.save(book);
            displayerImpl.displayMessage("The book with id = " + bookId + " has been updated");
        }
    }

    @Override
    public void deleteEntity(List<Long> bookIds, List<Long> authorIds, List<Long> genreIds, List<Long> comments) {
        if (genreIds != null) {
            genreIds.forEach(it -> {
                genreRepoJpa.deleteById(it);
            });
        }
        if (authorIds != null) {
            authorIds.forEach(it -> {
                authorRepoJpa.deleteById(it);
            });
        }
        if (comments != null) {
            comments.forEach(it -> {
                commentRepoJpa.deleteById(it);
            });
        }
        if (bookIds != null) {
            bookIds.forEach(it -> {
                bookRepoJpa.deleteById(it);
            });
        }
        displayerImpl.displayMessage("entities have been deleted");
    }

    @Override
    public void addComment(Long bookId, String comment) {
        if (bookId != null && bookId != 0L) {
            if (comment.isBlank()) {
                displayerImpl.displayMessage("book comment is empty!");
            } else {
                BookComment bookComment = commentRepoJpa.save(BookComment.builder().comment(comment).build());
                Book book = bookRepoJpa.getById(bookId);
//                BookComment bookComment = new BookComment();
                bookComment.setComment(comment);
                book.getComments().add(bookComment);
                bookRepoJpa.save(book);
            }
        } else {
            displayerImpl.displayMessage("insert book id to add comment!");
        }
    }
}
