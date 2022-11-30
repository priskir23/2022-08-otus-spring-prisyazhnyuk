package ru.otus.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.entities.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> { }
