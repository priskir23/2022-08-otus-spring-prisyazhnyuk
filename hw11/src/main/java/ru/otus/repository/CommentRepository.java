package ru.otus.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.entities.BookComment;

import java.util.Optional;

public interface CommentRepository extends ReactiveMongoRepository<BookComment, String> {
    Optional<BookComment> findByComment(String comment);
}
