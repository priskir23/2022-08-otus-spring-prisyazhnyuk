package ru.otus.repo;

import org.springframework.stereotype.Repository;
import ru.otus.entities.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CommentRepoJpa implements CommentRepository {
    @PersistenceContext
    private EntityManager em;


    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() == null) {
            em.persist(bookComment);
            return bookComment;
        }
        return em.merge(bookComment);
    }

    @Override
    public BookComment getById(long id) {
        return em.find(BookComment.class, id);
    }


    @Override
    public List<BookComment> getAll() {
        TypedQuery<BookComment> select = em.createQuery("select a from BookComment a", BookComment.class);
        return select.getResultList();
    }


    @Override
    public void deleteById(long id) {
        em.remove(getById(id));
    }
}
