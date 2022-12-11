package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {@NamedAttributeNode("genre")})
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private List<BookComment> comments;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 5)
    @ManyToMany(targetEntity = Author.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "author_to_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;


    @Override
    public String toString() {
        return "Book{" +
                " id=" + id +
                ", name='" + name + '\'' +
                ", genre=" + (genre != null ? genre.getName() : "") +
                '}';
    }
}
