package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Override
    <S extends Genre> S save(S entity);

    @Override
    Optional<Genre> findById(Long aLong);

    @Override
    List<Genre> findAll();

    @Override
    void deleteById(Long aLong);
}
