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
import java.util.*;

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
            Optional<Genre> genre = genreRepoJpa.findById(genreId);
            if (genre.isPresent()) {
                bookBuilder = bookBuilder.genre(genre.get());
            }
        }

        if (authorsId != null && !authorsId.isEmpty()) {
            Set<Author> authors = new HashSet<>(authorRepoJpa.findAllById(authorsId));
            bookBuilder = bookBuilder.authors(authors);
        }
        return bookRepoJpa.save(bookBuilder.build());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public Map<String, List<?>> showEntities(boolean showBook, boolean showAuthor, boolean showGenre) {
        Map<String, List<?>> map = new HashMap<>();
        if (showBook) {
            map.put("book", bookRepoJpa.findAll());
        }
        if (showAuthor) {
            map.put("author", authorRepoJpa.findAll());
        }
        if (showGenre) {
            map.put("genre", genreRepoJpa.findAll());
        }
        return map;
    }

    @Transactional
    @Override
    public Book updateBook(String bookName, Long bookId, Long genreId, Set<Long> authorsId) {
        Optional<Book> bookOpt = bookRepoJpa.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setName(bookName);
            if (genreId != null) {
                Optional<Genre> genre = genreRepoJpa.findById(genreId);
                genre.ifPresent(book::setGenre);
            }
            if (authorsId != null && !authorsId.isEmpty()) {
                Set<Author> authors = new HashSet<>(authorRepoJpa.findAllById(authorsId));
                book.setAuthors(authors);
            }
            return bookRepoJpa.save(book);
        }
        return null;
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
        Optional<Book> bookOptional = bookRepoJpa.findById(bookId);
        if (bookOptional.isPresent()) {
            bookComment.setComment(comment);
            Book book = bookOptional.get();
            book.getComments().add(bookComment);
            bookRepoJpa.save(book);
            return bookComment;
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public List<BookComment> getComments(Long bookId) {
        Optional<Book> bookOptional = bookRepoJpa.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            //для подгрузки комментов в рамках транзакции
            book.getComments().size();
            return book.getComments();
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Override
    public Set<Author> getAuthors(Long bookId) {
        Optional<Book> bookOptional = bookRepoJpa.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            //для подгрузки авторов в рамках транзакции
            book.getAuthors().size();
            return book.getAuthors();
        }
        return null;
    }
}
