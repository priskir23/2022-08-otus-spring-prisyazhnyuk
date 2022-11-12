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

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class DbServiceImpl implements DbService {

    private AuthorRepository authorRepoJpa;
    private BookRepository bookRepoJpa;
    private GenreRepository genreRepoJpa;
    private CommentRepository commentRepoJpa;

    @Transactional
    @Override
    public Book addBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId) {
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
        return bookRepoJpa.save(bookBuilder.build());
    }

    @Override
    public Map<String, List<?>> showEntities(boolean showBook, boolean showAuthor, boolean showGenre) {
        Map<String, List<?>> map = new HashMap<>();
        if (showBook) {
            map.put("book", bookRepoJpa.getAll());
        }
        if (showAuthor) {
            map.put("author", authorRepoJpa.getAll());
        }
        if (showGenre) {
            map.put("genre", genreRepoJpa.getAll());
        }
        return map;
    }

    @Transactional
    @Override
    public Book updateBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId) {
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
        return bookRepoJpa.save(book);
    }

    @Transactional
    @Override
    public void deleteEntity(List<Long> bookIds, List<Long> authorIds, List<Long> genreIds, List<Long> comments) {
        genreIds.forEach(it -> {
            genreRepoJpa.deleteById(it);
        });
        authorIds.forEach(it -> {
            authorRepoJpa.deleteById(it);
        });
        comments.forEach(it -> {
            commentRepoJpa.deleteById(it);
        });
        bookIds.forEach(it -> {
            bookRepoJpa.deleteById(it);
        });
    }

    @Transactional
    @Override
    public BookComment addComment(Long bookId, String comment) {
        BookComment bookComment = commentRepoJpa.save(BookComment.builder().comment(comment).build());
        Book book = bookRepoJpa.getById(bookId);
        bookComment.setComment(comment);
        book.getComments().add(bookComment);
        bookRepoJpa.save(book);
        return bookComment;
    }

    @Transactional
    @Override
    public List<BookComment> getComments(Long bookId) {
        Book book = bookRepoJpa.getById(bookId);
        if (book == null) {
            return null;
        }
        //для подгрузки комментов в рамках транзакции
        book.getComments().size();
        return book.getComments();
    }

    @Transactional
    @Override
    public Set<Author> getAuthors(Long bookId) {
        Book book = bookRepoJpa.getById(bookId);
        if (book == null) {
            return null;
        }
        //для подгрузки авторов в рамках транзакции
        book.getAuthors().size();
        return book.getAuthors();
    }
}
