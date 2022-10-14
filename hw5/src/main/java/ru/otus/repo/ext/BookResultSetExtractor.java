package ru.otus.repo.ext;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BookResultSetExtractor implements ResultSetExtractor<List<Book>> {
    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        ArrayList<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = getBook(rs);
            if (book == null) {
                continue;
            }
            books.add(book);
        }
        return books;
    }

    public static Book getBook(ResultSet rs) throws SQLException {
        Long id = rs.getLong("bid");
        String name = rs.getString("book_name");
        if (!isNameFilled(name)) {
            return null;
        }
        return Book.builder()
                .id(id)
                .name(name)
                        .genre(getGenre(rs))
                                .authors(getAuthors(rs))
                .build();
    }

    private static List<Author> getAuthors(ResultSet rs) throws SQLException {
        String idsArray = rs.getString("ids");
        String namesArray = rs.getString("names");
        if (!isNameFilled(idsArray) || !isNameFilled(namesArray)) {
            return null;
        }
        List<Long> ids = Arrays.stream(idsArray.split(",")).map(Long::parseLong).toList();
        List<String> names = Arrays.stream(namesArray.split(",")).toList();
        return IntStream.range(0, Math.min(ids.size(), names.size()))
                .mapToObj(idx -> new Author(ids.get(idx), names.get(idx)))
                .collect(Collectors.toList());
    }

    private static Genre getGenre(ResultSet rs) throws SQLException {
        Long id = rs.getLong("gid");
        String name = rs.getString("genre_name");
        if (!isNameFilled(name)) {
            return null;
        }
        return new Genre(id, name);
    }

    private static boolean isNameFilled(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return true;
    }
}
