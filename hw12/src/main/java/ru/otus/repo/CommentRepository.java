package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.BookComment;

public interface CommentRepository extends JpaRepository<BookComment, Long> {}
