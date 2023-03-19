package ru.vasiljev.springcourse.project3restcontroller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class SensorDTO {
    @NotEmpty(message = "Имя не должно быть пустым")
    @NotNull(message = "Имя не должно быть пустым")
    @Size(min = 3, max = 30, message = "Имя должно содержать от 3 до 30 символов")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
