package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.BookComment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<BookComment, Long> {


    @Override
    <S extends BookComment> S save(S entity);

    @Override
    Optional<BookComment> findById(Long aLong);

    @Override
    List<BookComment> findAll();

    @Override
    void deleteById(Long aLong);
}
