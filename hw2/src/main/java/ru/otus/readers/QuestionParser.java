package ru.otus.readers;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.entities.Question;


@Getter
@Component
public class QuestionParser extends EntitiesCsvParser<Question> {

    @Override
    void doIntermediateOperations(CsvToBeanBuilder<Question> builder) {
        builder.withType(getType())
                .withSeparator(';')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withOrderedResults(true);
    }
}
