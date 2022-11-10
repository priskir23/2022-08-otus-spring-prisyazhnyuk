package ru.otus.repo;

import org.springframework.stereotype.Repository;
import ru.otus.entities.Book;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookRepoJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Transactional
    @Override
    public Book getById(long id) {
        return em.find(Book.class, id);
    }

    @Transactional
    @Override
    public List<Book> getAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
        TypedQuery<Book> query = em.createQuery("select s from Book s join fetch s.comments", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        em.remove(getById(id));
    }
}
