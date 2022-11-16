package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Override
    <S extends Author> S save(S entity);

    @Override
    List<Author> findAll();

    @Override
    Optional<Author> findById(Long aLong);

    @Override
    List<Author> findAllById(Iterable<Long> longs);

    @Override
    void deleteById(Long aLong);
}
