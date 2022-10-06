package ru.otus.readers;

import ru.otus.entities.Question;
import ru.otus.services.IOService;

import java.util.Scanner;


public class AnswerReader<T extends Question> {
    private final Scanner scanner;

    public AnswerReader(IOService ioService) {
        this.scanner = new Scanner(ioService.getIn());
    }

    public boolean checkAnswer(T question) {
        var answer = scanner.nextLine();
        try {
            var inputPos = Integer.parseInt(answer);
            if (inputPos == question.getAnsPosition()) {
                return true;
            }
        } catch (NumberFormatException e) {
            var rightAnswer = question.getOptions()
                    .get(question.getAnsPosition() - 1)
                    .toUpperCase();
            if (rightAnswer.equals(answer.trim().toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
