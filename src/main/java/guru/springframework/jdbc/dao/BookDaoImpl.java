package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<Book> findAll() {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Book> query = em.createNamedQuery("book_find_all", Book.class);

            return query.getResultList();
        }
    }

    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();

        return em.find(Book.class, id);
    }

    @Override
    public Book findBookByTitle(String title) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Book> query = em.createNamedQuery("find_by_title", Book.class);
            query.setParameter("title", title);

            return query.getSingleResult();
        }
    }

    @Override
    public Book findBookByTitleCriteria(String title) {
        try (EntityManager em = getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);

            Root<Book> root = cq.from(Book.class);

            ParameterExpression<String> titleParam = cb.parameter(String.class);
            Predicate titlePredicate = cb.equal(root.get("title"), titleParam);

            cq.select(root).where(titlePredicate);

            TypedQuery<Book> query = em.createQuery(cq);
            query.setParameter(titleParam, title);

            return query.getSingleResult();
        }
    }

    @Override
    public Book findBookByTitleNative(String title) {
        try (EntityManager em = getEntityManager()) {
            Query query = em.createNativeQuery("SELECT * FROM book b WHERE b.title=:title", Book.class);

            query.setParameter("title", title);

            return (Book) query.getSingleResult();
        }
    }

    @Override
    public Book findByISBN(String isbn) {
        try (EntityManager em = getEntityManager()) {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn=:isbn", Book.class);
            query.setParameter("isbn", isbn);

            return query.getSingleResult();
        }
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
        em.close();

        return book;
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.merge(book);
        em.getTransaction().commit();
        em.close();

        return book;
    }

    @Override
    public void deleteBookById(Long id) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.remove(em.find(Book.class, id));
        em.getTransaction().commit();
        em.close();
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
