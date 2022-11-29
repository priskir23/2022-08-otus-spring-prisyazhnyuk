package ru.otus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.entities.BookComment;

import java.util.Optional;

public interface CommentRepository extends MongoRepository<BookComment, String> {
    Optional<BookComment> findByComment(String comment);
}
