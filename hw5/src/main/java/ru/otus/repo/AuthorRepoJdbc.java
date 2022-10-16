package ru.otus.repo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.entities.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class AuthorRepoJdbc implements AuthorRepository{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public long insert(Author author) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", author.getName());
        if (author.getId() != null) {
            mapSqlParameterSource.addValue("id", author.getId());
            namedParameterJdbcOperations.update("insert into author (id, author_name) values (:id, :name)",
                    mapSqlParameterSource);
            return author.getId();
        } else {
            //если была вставка с указанием ID, то в следующий раз вставка только по имени не будет работать, требуется указать alter
            namedParameterJdbcOperations.getJdbcOperations().execute("alter table author alter column id restart with (select max(id) from author) + 1;");
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            namedParameterJdbcOperations.update("insert into author (author_name) values (:name)",
                    mapSqlParameterSource, generatedKeyHolder);
            return generatedKeyHolder.getKey().longValue();
        }
    }

    @Override
    public void update(Author author) {
        namedParameterJdbcOperations.update("update author set author_name = :name where id = :id",
                Map.of("id", author.getId(), "name", author.getName()));
    }

    @Override
    public Author getById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select id, author_name from author where id = :id", Map.of("id", id), new AuthorMapper()
        );
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("select id, author_name from author", new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from author_to_book where author_id = :id", Map.of("id", id));
        namedParameterJdbcOperations.update("delete from author where id = :id", Map.of("id", id));
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("author_name");
            return new Author(id, name);
        }
    }
}
