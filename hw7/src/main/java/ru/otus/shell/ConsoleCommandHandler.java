package ru.otus.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.entities.Author;
import ru.otus.entities.Book;
import ru.otus.entities.BookComment;
import ru.otus.service.DbService;
import ru.otus.service.Displayer;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ShellComponent
@AllArgsConstructor
public class ConsoleCommandHandler {
    private final DbService dbService;

    private Displayer displayerImpl;

    @ShellMethod(value = "Add the Book command", key = {"a", "add"})
    public void add(@ShellOption(value = {"-b", "--book"}, defaultValue = ShellOption.NULL) String bookName,
                    @ShellOption(value = {"-i", "--bookId"}, defaultValue = ShellOption.NULL) Long bookId,
                    @ShellOption(value = {"-a", "--authorId"}, defaultValue = "") Set<Long> authorsId,
                    @ShellOption(value = {"-g", "--genreId"}, defaultValue = ShellOption.NULL) Long genreId) {
        if (bookName == null || bookName.isEmpty()) {
            displayerImpl.displayMessage("insert name for the book");
        } else {
            Book book = dbService.addBook(bookName, bookId, genreId, authorsId);
            displayerImpl.displayMessage("The book " + book + " has been added");
        }
    }

    @ShellMethod(value = "Show all entities (a - author, b - book, g - genre)", key = "sa")
    public void showAll(@ShellOption(arity = 1, value = {"-b", "--book"}, defaultValue = "false") boolean showBook,
                        @ShellOption(arity = 1, value = {"-a", "--author"}, defaultValue = "false") boolean showAuthor,
                        @ShellOption(arity = 1, value = {"-g", "--genre"}, defaultValue = "false") boolean showGenre) {
        Map<String, List<?>> entityNameToListMap = dbService.showEntities(showBook, showAuthor, showGenre);
        if (entityNameToListMap == null || entityNameToListMap.isEmpty()) {
            displayerImpl.displayMessage("there is no entity to show");
        } else {
            entityNameToListMap.forEach((key, value) -> {
                displayerImpl.displayEntities(value, key);
            });
        }
    }

    @ShellMethod(value = "Show all comments for book", key = "sc")
    public void showComments(@ShellOption(value = {"-i", "--bookId"}, defaultValue = ShellOption.NULL) Long bookId) {
        if (bookId == null) {
            displayerImpl.displayMessage("insert bookId");
            return;
        }
        List<BookComment> comments = dbService.getComments(bookId);
        if (comments != null) {
            displayerImpl.displayEntities(comments, "book comments");
        } else {
            displayerImpl.displayMessage("There is no book with given id");
        }
    }

    @ShellMethod(value = "Show all authors for book", key = "sau")
    public void showAuthors(@ShellOption(value = {"-i", "--bookId"}, defaultValue = ShellOption.NULL) Long bookId) {
        if (bookId == null) {
            displayerImpl.displayMessage("insert bookId");
            return;
        }
        Set<Author> authors = dbService.getAuthors(bookId);
        displayerImpl.displayEntities(authors.stream().toList(), "authors");
    }

    @ShellMethod(value = "Update book by id", key = "ub")
    public void update(@ShellOption(value = {"-i", "--id"}, defaultValue = ShellOption.NULL) Long bookId,
                       @ShellOption(value = {"-b", "--book"}, defaultValue = ShellOption.NULL) String bookName,
                       @ShellOption(help = "optional to chain with authors", arity = 1, value = {"-a", "--author"}, defaultValue = "") Set<Long> authorsId,
                       @ShellOption(help = "optional to chain with genre", arity = 1, value = {"-g", "--genre"}, defaultValue = ShellOption.NULL) Long genreId) {
        if (bookId == null) {
            displayerImpl.displayMessage("insert id of the book");
        } else {
            Book book = dbService.updateBook(bookName, bookId, genreId, authorsId);
            if (book != null) {
                displayerImpl.displayMessage("The book with id = " + book.getId() + " has been updated");
            } else {
                displayerImpl.displayMessage("There is no book with given id");
            }
        }
    }

    @ShellMethod(value = "Add comment to book", key = "comm")
    public void comment(@ShellOption(value = {"-i", "--id"}, defaultValue = ShellOption.NULL) Long bookId,
                        @ShellOption(value = {"-c", "--comment"}, defaultValue = ShellOption.NULL) String comment) {
        if (bookId != null && bookId != 0L) {
            if (comment.isBlank()) {
                displayerImpl.displayMessage("book comment is empty!");
            } else {
                BookComment bookComment = dbService.addComment(bookId, comment);
                if (bookComment != null) {
                    displayerImpl.displayMessage("Book comment with id = " + bookComment.getId() + " has been added");
                } else {
                    displayerImpl.displayMessage("There is no book with given id");
                }
            }
        } else {
            displayerImpl.displayMessage("insert book id to add comment!");
        }
    }

    @ShellMethod(value = "Delete entities by id (a - author, b - book, g - genre, c - comments)", key = "del")
    public void deleteById(@ShellOption(value = {"-b", "--book"}, defaultValue = "") List<Long> bookIds,
                           @ShellOption(value = {"-a", "--author"}, defaultValue = "") List<Long> authorIds,
                           @ShellOption(value = {"-c", "--comments"}, defaultValue = "") List<Long> commIds,
                           @ShellOption(value = {"-g", "--genre"}, defaultValue = "") List<Long> genreIds) {
        dbService.deleteEntity(bookIds, authorIds, genreIds, commIds);
        displayerImpl.displayMessage("entities have been deleted");
    }


}
