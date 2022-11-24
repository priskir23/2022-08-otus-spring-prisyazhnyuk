package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {}
