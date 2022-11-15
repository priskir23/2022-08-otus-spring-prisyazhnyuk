package ru.otus.repo;

import ru.otus.entities.BookComment;

import java.util.List;

public interface CommentRepository {
    BookComment save(BookComment bookComment);
    BookComment getById(long id);
    List<BookComment> getAll();
    void deleteById(long id);
}
