package ru.otus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.entities.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {}
