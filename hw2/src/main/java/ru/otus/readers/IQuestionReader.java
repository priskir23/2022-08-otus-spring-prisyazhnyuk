package ru.otus.readers;

import ru.otus.entities.Question;

import java.util.List;

public interface IQuestionReader {
    List<Question> getQuestions() throws Exception;
}
