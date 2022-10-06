package ru.otus.displayers;

import org.springframework.context.MessageSource;
import ru.otus.config.AppProps;
import ru.otus.entities.Question;
import ru.otus.services.IOService;

import java.io.PrintStream;
import java.util.stream.IntStream;

public class QuestionDisplayerImpl<T extends Question> implements QuestionDisplayer<T> {
    private final PrintStream writer;
    private final MessageSource messageSource;
    private final AppProps appProps;

    public QuestionDisplayerImpl(IOService ioService, MessageSource messageSource, AppProps appProps) {
        this.writer = ioService.getOut();
        this.messageSource = messageSource;
        this.appProps = appProps;
    }

    @Override
    public void displayQuestion(T question, int position) {
        writer.println(messageSource.getMessage("question.template",
                new String[]{String.valueOf(position + 1), question.getQuestion()}, appProps.locale()));
        IntStream.range(0, question.getOptions().size()).forEach(optIdx ->
                writer.printf("%s) %s%n", optIdx + 1, question.getOptions().get(optIdx))
        );
        writer.println(messageSource.getMessage("question.ask", null, appProps.locale()));
        writer.println();
        writer.flush();
    }
}
