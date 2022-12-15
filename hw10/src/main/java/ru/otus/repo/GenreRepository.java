package ru.otus.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entities.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> { }
