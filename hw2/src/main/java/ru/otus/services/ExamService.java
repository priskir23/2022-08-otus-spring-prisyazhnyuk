package ru.otus.services;

import java.io.InputStream;
import java.io.PrintStream;

public interface ExamService {
    void startExam(InputStream in, PrintStream writer);
}
