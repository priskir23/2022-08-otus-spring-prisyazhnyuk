package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.entities.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select s from Book s left join fetch s.genre")
    @Override
    List<Book> findAll();
}
