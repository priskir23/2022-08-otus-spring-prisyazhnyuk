package ru.otus;

import org.junit.Test;
import ru.otus.entities.Question;
import ru.otus.readers.AnswerReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleTest {

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
