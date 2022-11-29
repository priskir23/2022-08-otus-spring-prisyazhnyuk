package ru.otus.service;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.entities.Genre;
import ru.otus.repo.AuthorRepository;
import ru.otus.repo.BookRepository;
import ru.otus.repo.CommentRepository;
import ru.otus.repo.GenreRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class DbServiceImpl implements DbService {

    private AuthorRepository authorRepo;
    private BookRepository bookRepo;
    private GenreRepository genreRepo;
    private CommentRepository commentRepo;

    
    @Override
    public Book addBook(String bookName, String bookId, String genreId, List<String> authorsId) {
        Book.BookBuilder bookBuilder = Book.builder()
                .name(bookName)
                .id(bookId);
        if (genreId != null) {
            Optional<Genre> genre = genreRepo.findById(genreId);
            if (genre.isPresent()) {
                bookBuilder = bookBuilder.genre(genre.get());
            }
        }

        if (authorsId != null && !authorsId.isEmpty()) {
            List<Author> authorsList = new ArrayList<>();
            Iterable<Author> allById = authorRepo.findAllById(authorsId);
            allById.forEach(authorsList::add);

            bookBuilder = bookBuilder.authors(authorsList);
        }
        return bookRepo.save(bookBuilder.build());
    }

    
    @Override
    public Book updateBook(String bookName, String bookId, String genreId, List<String> authorsId) {
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setName(bookName);
            if (genreId != null) {
                Optional<Genre> genre = genreRepo.findById(genreId);
                genre.ifPresent(book::setGenre);
            }
            if (authorsId != null && !authorsId.isEmpty()) {
                List<Author> authorsList = new ArrayList<>();
                Iterable<Author> allById = authorRepo.findAllById(authorsId);
                allById.forEach(authorsList::add);

                book.setAuthors(authorsList);
            }
            return bookRepo.save(book);
        }
        return null;
    }

    @Override
    public void deleteEntity(List<String> bookIds, List<String> authorIds, List<String> genreIds, List<String> comments) {
        genreIds.forEach(it -> {
            genreRepo.deleteById(it);
        });
        authorIds.forEach(it -> {
            authorRepo.deleteById(it);
        });
        comments.forEach(it -> {
            commentRepo.deleteById(it);
        });
        bookIds.forEach(it -> {
            bookRepo.deleteById(it);
        });
    }

    
    @Override
    public BookComment addComment(String bookId, String comment) {
        BookComment bookComment = commentRepo.save(BookComment.builder().comment(comment).build());
        Optional<Book> bookOptional = bookRepo.findById(bookId);
        if (bookOptional.isPresent()) {
            bookComment.setComment(comment);
            Book book = bookOptional.get();
            book.getComments().add(bookComment);
            bookRepo.save(book);
            return bookComment;
        }
        return null;
    }


    
    @Override
    public List<BookComment> getComments(String bookId) {
        Optional<Book> bookOptional = bookRepo.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return book.getComments();
        }
        return null;
    }

    
    @Override
    public List<Author> getAuthorsById(String bookId) {
        Optional<Book> bookOptional = bookRepo.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            return book.getAuthors();
        }
        return null;
    }

    
    @Override
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepo.findAll();
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepo.findAll();
    }
}
