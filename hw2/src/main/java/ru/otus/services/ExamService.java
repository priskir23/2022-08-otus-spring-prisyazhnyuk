package ru.otus.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.entities.Question;
import ru.otus.readers.IQuestionReader;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class ExamService {

    private IQuestionReader reader;
    private Integer threshold;

    public ExamService(IQuestionReader reader, @Value("${exam.threshold}") Integer threshold) {
        this.reader = reader;
        this.threshold = threshold;
    }

    public void startExam() {
        var scanner = new Scanner(System.in);
        try {
            List<Question> questions = reader.getQuestions();
            AtomicInteger rightCount = new AtomicInteger();
            Collections.shuffle(questions);
            IntStream.range(0, questions.size()).forEach(idx -> {
                var question = questions.get(idx);
                System.out.printf("Question %s - %s%n", idx + 1, question.getQuestion());
                IntStream.range(0, question.getOptions().size()).forEach(optIdx ->
                        System.out.printf("%s) %s%n", optIdx + 1, question.getOptions().get(optIdx))
                );
                System.out.println("which option?");
                System.out.println();
                var answer = scanner.nextLine();
                try {
                    var inputPos = Integer.parseInt(answer);
                    if (inputPos == question.getAnsPosition()) {
                        rightCount.getAndIncrement();
                    }
                } catch (NumberFormatException e) {
                    var rightAnswer = question.getOptions().get(question.getAnsPosition() - 1);
                    if (rightAnswer.equals(answer.trim())) {
                        rightCount.getAndIncrement();
                    }
                }
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
}
