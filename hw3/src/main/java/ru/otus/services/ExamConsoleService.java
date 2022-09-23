package ru.otus.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.otus.config.AppProps;
import ru.otus.displayers.QuestionDisplayerImpl;
import ru.otus.entities.Question;
import ru.otus.readers.AnswerReader;
import ru.otus.readers.QuestionReader;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
//CommandLineRunner запускается во время теста, поэтому требуется его отключить
@Profile("!test")
public class ExamConsoleService<T extends Question> implements ExamService, CommandLineRunner {

    private final MessageSource messageSource;
    private final AppProps appProps;
    private final QuestionReader<T> reader;

    public ExamConsoleService(QuestionReader<T> reader, AppProps appProps, MessageSource msgSource) {
        this.reader = reader;
        this.appProps = appProps;
        messageSource = msgSource;
    }

    @Override
    public void startExam(InputStream in, PrintStream out) {
        try {
            var answerReader = new AnswerReader<T>(in);
            var questionDisplayer = new QuestionDisplayerImpl<T>(out, messageSource, appProps);
            List<T> questions = reader.getQuestions();
            AtomicInteger rightCount = new AtomicInteger();
            IntStream.range(0, questions.size()).forEach(idx -> {
                var question = questions.get(idx);
                showQuestion(questionDisplayer, idx, question);
                getResult(answerReader, rightCount, question);
            });

            out.println(messageSource.getMessage("result.end", null, appProps.locale()));
            if (rightCount.get() >= appProps.threshold()) {
                out.println(messageSource.getMessage("result.right", new String[]{String.valueOf(rightCount.get())}, appProps.locale()));
            } else {
                out.println(messageSource.getMessage("result.false", null, appProps.locale()));
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void getResult(AnswerReader<T> answerReader, AtomicInteger rightCount, T question) {
        if (answerReader.checkAnswer(question)) {
            rightCount.getAndIncrement();
        }
    }

    private void showQuestion(QuestionDisplayerImpl<T> questionDisplayer, int idx, T question) {
        questionDisplayer.displayQuestion(question, idx);
    }

    @Override
    public void run(String... args) throws Exception {
        startExam(System.in, System.out);
    }
}
