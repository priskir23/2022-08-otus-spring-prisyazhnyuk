package ru.otus.repo;

import org.springframework.stereotype.Repository;
import ru.otus.entities.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class GenreRepoJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            em.persist(genre);
            return genre;
        }
        return em.merge(genre);
    }

    @Transactional
    @Override
    public Genre getById(long id) {
        return em.find(Genre.class, id);
    }

    @Transactional
    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> select = em.createQuery("select a from Genre a", Genre.class);
        return select.getResultList();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        em.remove(getById(id));
    }
}
