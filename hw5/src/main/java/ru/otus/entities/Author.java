package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "Author:" +
                " id=" + id +
                ", name='" + name + '\'';
    }
}
