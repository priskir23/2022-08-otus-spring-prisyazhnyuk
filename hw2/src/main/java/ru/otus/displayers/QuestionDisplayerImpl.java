package ru.otus.displayers;

import lombok.AllArgsConstructor;
import ru.otus.entities.Question;

import java.io.PrintStream;
import java.util.stream.IntStream;

@AllArgsConstructor
public class QuestionDisplayerImpl<T extends Question> implements QuestionDisplayer<T> {
    private PrintStream writer;

    @Override
    public void displayQuestion(T question, int position) {
        writer.printf("Question %s - %s%n", position + 1, question.getQuestion());
        IntStream.range(0, question.getOptions().size()).forEach(optIdx ->
                writer.printf("%s) %s%n", optIdx + 1, question.getOptions().get(optIdx))
        );
        writer.println("which option?");
        writer.println();
        writer.flush();
    }
}
