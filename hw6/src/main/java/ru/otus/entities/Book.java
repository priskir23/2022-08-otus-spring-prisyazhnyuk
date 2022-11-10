package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
@NamedEntityGraph(name = "book-entity-graph",
        attributeNodes = {@NamedAttributeNode("name")})
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "book_name")
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private List<BookComment> comments;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 5)
    @ManyToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
                ", authors=" + (CollectionUtils.isEmpty(authors) ? "" : authors.stream().map(Author::getName).collect(Collectors.joining(", "))) +
                ", comments=" + (CollectionUtils.isEmpty(comments) ? "" : comments.stream().map(BookComment::getComment).collect(Collectors.joining(", "))) +
                '}';
    }
}
