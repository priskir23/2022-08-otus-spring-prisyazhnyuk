package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    <S extends Book> S save(S entity);

    @Override
    Optional<Book> findById(Long aLong);

    @Query("select s from Book s join fetch s.genre")
    @Override
    List<Book> findAll();

    @Override
    void deleteById(Long aLong);
}
