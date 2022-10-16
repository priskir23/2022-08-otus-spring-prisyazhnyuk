package ru.otus.repo;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.entities.Book;
import ru.otus.repo.ext.BookResultSetExtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class BookRepoJdbc implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    
    @Override
    public long insert(Book book) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", book.getName());
        if (book.getId() != null) {
            mapSqlParameterSource.addValue("id", book.getId());
            namedParameterJdbcOperations.update("insert into book (id, book_name) values (:id, :name)",
                    mapSqlParameterSource);
            return book.getId();
        } else {
            namedParameterJdbcOperations.getJdbcOperations().execute("alter table book alter column id restart with (select max(id) from book) + 1;");
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            namedParameterJdbcOperations.update("insert into book (book_name) values (:name)",
                    mapSqlParameterSource, generatedKeyHolder);
            return generatedKeyHolder.getKey().longValue();
        }
    }

    @Override
    public void chainWithGenre(long bookId, long genreId) {
        namedParameterJdbcOperations.update("update book set genre_id = :genreId where id = :bookId",
                Map.of("genreId", genreId, "bookId", bookId));
    }

    @Override
    public void chainWithAuthors(long bookId, List<Long> authorIds) {
        namedParameterJdbcOperations.getJdbcOperations().batchUpdate("insert into author_to_book (author_id, book_id) values (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, authorIds.get(i));
                ps.setLong(2, bookId);
            }
            @Override
            public int getBatchSize() {
                return authorIds.size();
            }
        });
//        namedParameterJdbcOperations.update("insert into author_to_book (author_id, book_id) values (:authorId, :bookId)",
//                Map.of("authorId", authorId, "bookId", bookId));
    }

    @Override
    public void update(Book book) {
        namedParameterJdbcOperations.update("update book set book_name = :name where id = :id",
                Map.of("id", book.getId(), "name", book.getName()));
    }

    @Override
    public Book getById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                """
                        select b.id bid, b.book_name, g.id gid, g.genre_name, \s
                        listagg(a.id) ids, listagg(a.author_name) names \s
                        from book b 
                        left join author_to_book ab on b.id = ab.book_id 
                        left join author a on a.id = ab.author_id 
                        left join genre g on b.genre_id = g.id 
                        where b.id = :id 
                        group by b.id 
                        """,
                Map.of("id", id),
                new BookMapper()
        );
    }

    @Override
    public List<Book> getAll() {
        List<Book> books =
                namedParameterJdbcOperations.query("""
                                select b.id bid, b.book_name, g.id gid, g.genre_name, \s
                                listagg(a.id) ids, listagg(a.author_name) names \s
                                from book b 
                                left join author_to_book ab on b.id = ab.book_id 
                                left join author a on a.id = ab.author_id 
                                left join genre g on b.genre_id = g.id 
                                group by b.id 
                                """,
                        new BookResultSetExtractor());
        Objects.requireNonNull(books);
        return books;
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from author_to_book where book_id = :id", Map.of("id", id));
        namedParameterJdbcOperations.update("delete from book where id = :id", Map.of("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return BookResultSetExtractor.getBook(rs);
        }
    }

    private record BookAuthorRelation(long bookId, long authorId) {}
}
