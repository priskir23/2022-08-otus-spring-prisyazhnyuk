package ru.otus.repo;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.entities.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
public class GenreRepoJdbc implements GenreRepository{
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public long insert(Genre genre) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", genre.getName());
        if (genre.getId() != null) {
            mapSqlParameterSource.addValue("id", genre.getId());
            namedParameterJdbcOperations.update("insert into genre (id, genre_name) values (:id, :name)",
                    mapSqlParameterSource);
            return genre.getId();
        } else {
            //если была вставка с указанием ID, то в следующий раз вставка только по имени не будет работать, требуется указать alter
            namedParameterJdbcOperations.getJdbcOperations().execute("alter table genre alter column id restart with (select max(id) from genre) + 1;");
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            namedParameterJdbcOperations.update("insert into genre (genre_name) values (:name)",
                    mapSqlParameterSource, generatedKeyHolder);
            return generatedKeyHolder.getKey().longValue();
        }
    }

    @Override
    public void update(Genre genre) {
        namedParameterJdbcOperations.update("update genre set genre_name = :name where id = :id",
                Map.of("id", genre.getId(), "name", genre.getName()));
    }

    @Override
    public Genre getById(long id) {
        return namedParameterJdbcOperations.queryForObject(
                "select id, genre_name from genre where id = :id", Map.of("id", id), new GenreMapper()
        );
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query("select id, genre_name from genre", new GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("update book set genre_id = null where genre_id = :id", Map.of("id", id));
        namedParameterJdbcOperations.update("delete from genre where id = :id", Map.of("id", id));
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("genre_name");
            return new Genre(id, name);
        }
    }
}
