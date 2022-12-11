package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
