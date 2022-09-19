package ru.otus.entities;

import com.opencsv.bean.*;
import lombok.Data;

import java.util.List;

@Data
public class Question {
    @CsvBindByPosition(position = 0)
    private String question;
    @CsvBindByPosition(position = 1)
    private Integer ansPosition;
    @CsvBindAndSplitByPosition(position = 2, elementType = String.class, splitOn = ",")
    private List<String> options;
}

