package ru.otus.readers;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.config.AppProps;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvQuestionReader<T> implements QuestionReader<T> {

    private final AppProps appProps;
    private final EntitiesCsvParser<T> parser;

    public CsvQuestionReader(AppProps appProps, EntitiesCsvParser<T> questionParser) {
        this.appProps = appProps;
        this.parser = questionParser;
    }

    @Override
    public List<T> getQuestions() throws Exception {
        var fileIS = new ClassPathResource(getFilePath()).getInputStream();
        var reader = new InputStreamReader(new BOMInputStream(fileIS), StandardCharsets.UTF_8);
        return parser.getParsedEntities(reader);
    }

    private String getFilePath() {
        return appProps.path() + "_" + appProps.locale().toLanguageTag() + ".csv";
    }
}
