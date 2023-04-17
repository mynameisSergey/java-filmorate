package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    private int id;
    @NotNull(message = "Film name cannot be not null")
    @NotEmpty(message = "Film name cannot be not empty")
    @NotBlank(message = "Film name cannot be not blank")
    private String name;
    @NotEmpty
    @Size(max = 200, message = "Film description must be less than 200 characters.")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0L, message = "Film duration must be more that 0")
    private long duration;
    @NotNull(message = "Film need to be rated")
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}