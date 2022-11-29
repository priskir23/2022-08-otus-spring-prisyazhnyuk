package ru.otus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.entities.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    Optional<Book> findByName(String name);
}
