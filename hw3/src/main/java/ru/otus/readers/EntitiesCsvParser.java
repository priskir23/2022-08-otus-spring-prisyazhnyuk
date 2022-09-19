package ru.otus.readers;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class EntitiesCsvParser<T> {
    private final Class<T> type;

    public EntitiesCsvParser() {
        type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];

    }

    public final List<T> getParsedEntities(Reader reader) {
        CsvToBeanBuilder<T> builder = new CsvToBeanBuilder<>(reader);
        doIntermediateOperations(builder);
        List<T> parsedQuestions = builder.build().parse();
        Collections.shuffle(parsedQuestions);
        return parsedQuestions;
    }

    abstract void doIntermediateOperations(CsvToBeanBuilder<T> builder);

}
