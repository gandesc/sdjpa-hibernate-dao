package guru.springframework.jdbc.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NamedQueries({
        @NamedQuery(name = "book_find_all", query = "FROM Book"),
        @NamedQuery(name = "find_by_title", query = "FROM Book b WHERE b.title=:title")
})
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    private String publisher;
    private Long authorId;

    public Book(String title, String isbn, String publisher) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
    }

}
