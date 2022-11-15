package ru.otus.repo;

import org.springframework.stereotype.Repository;
import ru.otus.entities.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AuthorRepoJpa implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            em.persist(author);
            return author;
        }
        return em.merge(author);
    }


    @Override
    public Author getById(long id) {
        return em.find(Author.class, id);
    }


    @Override
    public Set<Author> getByIds(Set<Long> ids) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.id in :list", Author.class);
        query.setParameter("list", ids);
        return new HashSet<>(query.getResultList());
    }


    @Override
    public List<Author> getAll() {
        TypedQuery<Author> select_s_from_author = em.createQuery("select a from Author a", Author.class);
        return select_s_from_author.getResultList();
    }


    @Override
    public void deleteById(long id) {
        em.remove(getById(id));
    }
}
