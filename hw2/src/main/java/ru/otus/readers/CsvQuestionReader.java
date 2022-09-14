package ru.otus.readers;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvQuestionReader<T> implements QuestionReader<T> {

    private String filePath;
    private EntitiesCsvParser<T> parser;

    public CsvQuestionReader(@Value("${questions.path}") String filePath, EntitiesCsvParser<T> questionParser) {
        this.filePath = filePath;
        this.parser = questionParser;
    }

    @Override
    public List<T> getQuestions() throws Exception {
        var fileIS = new ClassPathResource(filePath).getInputStream();
        var reader = new InputStreamReader(new BOMInputStream(fileIS), StandardCharsets.UTF_8);
        return parser.getParsedEntities(reader);
    }
}
