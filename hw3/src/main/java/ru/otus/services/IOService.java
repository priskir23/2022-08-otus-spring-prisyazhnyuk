package ru.otus.services;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;
import java.io.PrintStream;

@Getter
@AllArgsConstructor
public class IOService {
    private InputStream in;
    private PrintStream out;
}
