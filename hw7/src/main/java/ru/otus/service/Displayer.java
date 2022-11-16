package ru.otus.service;

import java.util.List;

public interface Displayer {
    void displayMessage(String msg);

    <T> void displayEntities(List<T> entities, String entityName);
}
