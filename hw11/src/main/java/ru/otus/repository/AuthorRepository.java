package ru.otus.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.entities.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {}
