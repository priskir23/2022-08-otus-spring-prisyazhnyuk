package ru.otus.displayers;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import ru.otus.config.AppProps;
import ru.otus.entities.Question;

import java.io.PrintStream;
import java.util.stream.IntStream;

@AllArgsConstructor
public class QuestionDisplayerImpl<T extends Question> implements QuestionDisplayer<T> {
    private PrintStream writer;
    private MessageSource messageSource;
    private AppProps appProps;

    @Override
    public void displayQuestion(T question, int position) {
        writer.println(messageSource.getMessage("question.template",
                new String[] {String.valueOf(position + 1), question.getQuestion()}, appProps.locale()));
        IntStream.range(0, question.getOptions().size()).forEach(optIdx ->
                writer.printf("%s) %s%n", optIdx + 1, question.getOptions().get(optIdx))
        );
        writer.println(messageSource.getMessage("question.ask", null, appProps.locale()));
        writer.println();
        writer.flush();
    }
}
