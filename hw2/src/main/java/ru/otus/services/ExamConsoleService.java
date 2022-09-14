package ru.otus.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
public class ExamConsoleService<T extends Question> implements ExamService {

    private final QuestionReader<T> reader;
    private final Integer threshold;

    public ExamConsoleService(QuestionReader<T> reader, @Value("${exam.threshold}") Integer threshold) {
        this.reader = reader;
        this.threshold = threshold;
    }

    @Override
    public void startExam(InputStream in, PrintStream out) {
        try {
            var answerReader = new AnswerReader<T>(in);
            var questionDisplayer = new QuestionDisplayerImpl<T>(out);
            List<T> questions = reader.getQuestions();
            AtomicInteger rightCount = new AtomicInteger();
            IntStream.range(0, questions.size()).forEach(idx -> {
                var question = questions.get(idx);
                showQuestion(questionDisplayer, idx, question);
                getResult(answerReader, rightCount, question);
            });
            System.out.println("Exam is over!");
            if (rightCount.get() >= threshold) {
                System.out.printf("Exam passed with %s right answers%n", rightCount.get());
            } else {
                System.out.println("Exam failed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
