package ru.otus.entities;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.util.List;

@Data
public class Question{
        @CsvBindByName(column = "question")
        private String question;
        @CsvBindByName(column = "answer")
        private Integer ansPosition;
        @CsvBindAndSplitByName(column = "options", elementType = String.class, splitOn = ",")
        private List<String> options;
}

