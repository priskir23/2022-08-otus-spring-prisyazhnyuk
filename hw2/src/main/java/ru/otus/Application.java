package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.services.ExamConsoleService;

@ComponentScan
@Configuration
@PropertySource("classpath:application.properties")
public class Application {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Application.class);

        var examService = context.getBean(ExamConsoleService.class);
        examService.startExam(System.in, System.out);
    }
}
