package ru.otus.displayers;

public interface QuestionDisplayer<T> {
    void displayQuestion(T question, int position);
}
