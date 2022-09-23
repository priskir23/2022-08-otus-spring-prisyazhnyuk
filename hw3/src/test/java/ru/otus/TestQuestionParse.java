package ru.otus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.config.AppProps;
import ru.otus.displayers.QuestionDisplayerImpl;
import ru.otus.entities.Question;
import ru.otus.readers.CsvQuestionReader;
import ru.otus.readers.QuestionParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestQuestionParse {
    @Autowired
    private AppProps appProps;
    @Autowired
    private MessageSource source;

    @Autowired
    private CsvQuestionReader<Question> questionReader;

    @Before
    public void init() throws Exception {
    }

    @Test
    public void testParseFromString() {
        String s = "Question;1;first,second,third,fourth";
        QuestionParser questionParser = new QuestionParser();

        List<Question> parsedEntities = questionParser.getParsedEntities(new StringReader(s));
        assertEquals(1, parsedEntities.size());
        Question question = parsedEntities.get(0);
        assertEquals("Question", question.getQuestion());
        assertEquals(1, question.getAnsPosition().intValue());
        assertEquals(4, question.getOptions().size());
    }

    /**
     * в тестовых csv - файлах по 2 вопроса, разбор файл -> контейнер происходит без ошибок
     */
    @Test
    public void testParseFromFile() throws Exception {
        List<Question> questions = questionReader.getQuestions();
        assertEquals(2, questions.size());
    }

    /**
     * Тестирование функциональности отрисовки вопроса
     */
    @Test
    public void checkDisplay() throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String utf = StandardCharsets.UTF_8.name();
        QuestionDisplayerImpl<Question> questionDisplayer = new QuestionDisplayerImpl<>(new PrintStream(byteArrayOutputStream, true, utf), source, appProps);
        Question question = new Question();
        question.setQuestion("Повар спрашивает повара: какова твоя профессия?");
        question.setOptions(Arrays.asList("Милиционер", "Врач", "Повар"));
        question.setAnsPosition(3);
        questionDisplayer.displayQuestion(question, 1);
        String diplayedString = byteArrayOutputStream.toString(utf);
        String rightDisplay = """
                Вопрос 2 - Повар спрашивает повара: какова твоя профессия?\r
                1) Милиционер\r
                2) Врач\r
                3) Повар\r
                какой вариант?\r
                \r
                """;
        assertEquals(rightDisplay, diplayedString);
    }
}
