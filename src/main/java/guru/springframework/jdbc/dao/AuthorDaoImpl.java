package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jt on 8/28/21.
 */
@RequiredArgsConstructor
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final EntityManagerFactory emf;

    @Override
    public List<Author> findAll() {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Author> query = em.createNamedQuery("author_find_all", Author.class);

            return query.getResultList();
        }
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastName) {
        try (EntityManager em = getEntityManager()) {
            Query query = em.createQuery("SELECT a FROM Author a WHERE lastName like :lastName");
            query.setParameter("lastName", lastName + '%');

            return query.getResultList();
        }
    }

    @Override
    public Author getById(Long id) {
        return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        try (EntityManager em = getEntityManager();) {
            TypedQuery<Author> query = em.createNamedQuery("find_by_name", Author.class);
            query.setParameter("firstName", firstName)
                    .setParameter("lastName", lastName);

            return query.getSingleResult();
        }
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.persist(author);
        em.getTransaction().commit();

        em.close();

        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();
        em.joinTransaction();
        em.merge(author);
        em.flush();
        em.clear();

        em.close();

        return author;
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        Author author = em.find(Author.class, id);
        em.remove(author);
        em.getTransaction().commit();

        em.close();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
