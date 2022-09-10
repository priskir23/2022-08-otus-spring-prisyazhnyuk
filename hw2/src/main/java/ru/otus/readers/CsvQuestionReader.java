package ru.otus.readers;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.entities.Question;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvQuestionReader implements IQuestionReader {

    private String filePath;

    public CsvQuestionReader(@Value("${questions.path}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Question> getQuestions() throws Exception {
        var fileIS = new ClassPathResource(filePath).getInputStream();

        var reader = new InputStreamReader(new BOMInputStream(fileIS), StandardCharsets.UTF_8);
        return new CsvToBeanBuilder<Question>(reader)
                .withType(Question.class)
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withOrderedResults(true)
                .build().parse();

    }
}
