package ru.otus.shell;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.services.ExamService;

@ShellComponent
public class ConsoleCommandHandler {

    private String nick;

    private ExamService examService;

    public ConsoleCommandHandler(@Qualifier("ExamConsoleService")ExamService examService) {
        this.examService = examService;
    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String nick) {
        this.nick = nick;
        return String.format("Welcome: %s", nick);
    }

    @ShellMethod(value = "Start the exam", key = {"s", "start"})
    @ShellMethodAvailability(value = "isTestStartAvailable")
    public void start() {
        examService.startExam();
    }

    private Availability isTestStartAvailable() {
        return nick == null || nick.isBlank() ? Availability.unavailable("U SHOULD BE LOG IN FIRST"): Availability.available();
    }
}
