package ru.otus.services;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.config.AppProps;
import ru.otus.displayers.QuestionDisplayerImpl;
import ru.otus.entities.Question;
import ru.otus.readers.AnswerReader;
import ru.otus.readers.QuestionReader;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service("ExamConsoleService")
public class ExamConsoleService<T extends Question> implements ExamService {

    private final MessageSource messageSource;
    private final AppProps appProps;
    private final QuestionReader<T> reader;
    private final IOService ioService;


    public ExamConsoleService(QuestionReader<T> reader, IOService ioService, AppProps appProps, MessageSource msgSource) {
        this.reader = reader;
        this.appProps = appProps;
        messageSource = msgSource;
        this.ioService = ioService;
    }

    @Override
    public void startExam() {
        try {
            var answerReader = new AnswerReader<T>(ioService);
            var questionDisplayer = new QuestionDisplayerImpl<T>(ioService, messageSource, appProps);
            List<T> questions = reader.getQuestions();
            AtomicInteger rightCount = new AtomicInteger();
            IntStream.range(0, questions.size()).forEach(idx -> {
                var question = questions.get(idx);
                showQuestion(questionDisplayer, idx, question);
                getResult(answerReader, rightCount, question);
            });

            showResultText(rightCount, ioService.getOut());
        } catch (Exception e) {
            ioService.getOut().println(e.getMessage());
        }
    }

    private void showResultText(AtomicInteger rightCount, PrintStream out) {
        out.println(messageSource.getMessage("result.end", null, appProps.locale()));
        if (rightCount.get() >= appProps.threshold()) {
            out.println(messageSource.getMessage("result.right", new String[]{String.valueOf(rightCount.get())}, appProps.locale()));
        } else {
            out.println(messageSource.getMessage("result.false", null, appProps.locale()));
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
}
