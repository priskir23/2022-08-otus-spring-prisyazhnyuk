package ru.otus.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "Genre:" +
                " id=" + id +
                ", name='" + name + '\'';
    }
}
