package ru.otus.service;

import java.util.List;

public interface Displayer {
    public void displayMessage(String msg);
    public <T> void displayEntities(List<T> entities, String entityName);
}
