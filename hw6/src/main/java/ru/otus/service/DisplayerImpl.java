package ru.otus.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DisplayerImpl implements Displayer {
    private final OutService outService;

    @Override
    public void displayMessage(String msg) {
        outService.getOut().println(msg);
        displayBreak();
    }

    @Override
    public <T> void displayEntities(List<T> entities, String entityName) {
        if (entities == null || entities.isEmpty()) {
            outService.getOut().println("there is no " + entityName + " to show");
            return;
        }
        entities.forEach(it -> outService.getOut().println(it));
        displayBreak();
    }


    private void displayBreak() {
        outService.getOut().println("------------------------");
    }
}
