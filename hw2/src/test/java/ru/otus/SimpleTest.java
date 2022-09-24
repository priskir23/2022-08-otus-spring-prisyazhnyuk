package ru.otus;

import org.junit.Test;
import ru.otus.displayers.QuestionDisplayerImpl;
import ru.otus.entities.Question;
import ru.otus.readers.AnswerReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class SimpleTest {
    /**
     * Тестирование функциональности отрисовки вопроса
     */
    @Test
    public void checkDisplay() throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String utf = StandardCharsets.UTF_8.name();
        QuestionDisplayerImpl<Question> questionDisplayer = new QuestionDisplayerImpl<>(new PrintStream(byteArrayOutputStream, true, utf));
        Question question = new Question();
        question.setQuestion("Повар спрашивает повара: какова твоя профессия?");
        question.setOptions(Arrays.asList("Милиционер", "Врач", "Повар"));
        question.setAnsPosition(3);
        questionDisplayer.displayQuestion(question, 1);
        String diplayedString = byteArrayOutputStream.toString(utf);
        String rightDisplay = """
                Question 2 - Повар спрашивает повара: какова твоя профессия?\r
                1) Милиционер\r
                2) Врач\r
                3) Повар\r
                which option?\r
                \r
                """;
        assertEquals(rightDisplay, diplayedString);
    }

    /**
     * Тестирование проверки ответов испытуемого
     * Первый ответ (1 и Первый) верный
     * Второй - неверный
     */
    @Test
    public void checkQuestion() {
        Question question = new Question();
        question.setQuestion("Первый вариант ответа правильный.");
        question.setAnsPosition(1);
        question.setOptions(Arrays.asList("Первый", "Второй"));
        AnswerReader<Question> answerReader = new AnswerReader<>(new ByteArrayInputStream("""
                1\r
                2\r
                пеРвЫй\r
                ВТОрой\r
                """.getBytes(StandardCharsets.UTF_8)));
        assertTrue(answerReader.checkAnswer(question));
        assertFalse(answerReader.checkAnswer(question));
        assertTrue(answerReader.checkAnswer(question));
        assertFalse(answerReader.checkAnswer(question));
    }
}
