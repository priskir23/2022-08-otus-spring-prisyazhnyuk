package ru.otus.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.entities.Book;

import java.util.Optional;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Optional<Book> findByName(String name);
}
