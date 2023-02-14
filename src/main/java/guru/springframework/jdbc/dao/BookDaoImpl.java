package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();

        return em.find(Book.class, id);
    }

    @Override
    public Book findBookByTitle(String title) {
        EntityManager em = getEntityManager();

        String jpqlQuery = "SELECT b FROM Book b WHERE b.title=:title";
        TypedQuery<Book> query = em.createQuery(jpqlQuery, Book.class);
        query.setParameter("title", title);

        Book book =  query.getSingleResult();
        em.close();

        return book;
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
