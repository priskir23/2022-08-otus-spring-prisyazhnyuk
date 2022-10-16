package ru.otus.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.service.DbService;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class ConsoleCommandHandler {
    private final DbService dbService;

    @ShellMethod(value = "Add the Book command", key = {"a", "add"})
    public void add(@ShellOption(value = {"-b", "--book"}, defaultValue = ShellOption.NULL) String bookName,
                    @ShellOption(value = {"-i", "--bookId"}, defaultValue = ShellOption.NULL) Long bookId,
                    @ShellOption(value = {"-a", "--authorId"}, defaultValue = "") List<Long> authorsId,
                    @ShellOption(value = {"-g", "--genreId"}, defaultValue = ShellOption.NULL) Long genreId) {
        dbService.addBook(bookName, bookId, genreId, authorsId);
    }

    @ShellMethod(value = "Show all entities (a - author, b - book, g - genre)", key = "sa")
    public void showAll(@ShellOption(arity = 1, value = {"-b", "--book"}, defaultValue = "false") boolean showBook,
                        @ShellOption(arity = 1, value = {"-a", "--author"}, defaultValue = "false") boolean showAuthor,
                        @ShellOption(arity = 1, value = {"-g", "--genre"}, defaultValue = "false") boolean showGenre) {
        dbService.showEntities(showBook, showAuthor, showGenre);
    }

    @ShellMethod(value = "Update book by id", key = "ub")
    public void update(@ShellOption(value = {"-i", "--id"}, defaultValue = ShellOption.NULL) Long bookId,
                       @ShellOption(value = {"-b", "--book"}, defaultValue = ShellOption.NULL) String bookName,
                       @ShellOption(help = "optional to chain with authors", arity = 1, value = {"-a", "--author"}, defaultValue = "") List<Long> authorsId,
                       @ShellOption(help = "optional to chain with genre", arity = 1, value = {"-g", "--genre"}, defaultValue = ShellOption.NULL) Long genreId) {
        dbService.updateBook(bookName, bookId, genreId, authorsId);
    }

    @ShellMethod(value = "Delete entities by id (a - author, b - book, g - genre)", key = "del")
    public void deleteById(@ShellOption(value = {"-b", "--book"}, defaultValue = "") List<Long> bookIds,
                           @ShellOption(value = {"-a", "--author"}, defaultValue = "") List<Long> authorIds,
                           @ShellOption(value = {"-g", "--genre"}, defaultValue = "") List<Long> genreIds) {
        dbService.deleteEntity(bookIds, authorIds, genreIds);
    }


}
