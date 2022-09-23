package ru.otus.readers;

import java.util.List;

public interface QuestionReader<T> {
    List<T> getQuestions() throws Exception;
}
