package ru.vasiljev.springcourse.project3restcontroller.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MeasurementDTO {

    @NotNull(message = "Поле value не может быть пустым")
    @Min(value = -100, message = "Значение температуры не может быть меньше чем -100")
    @Max(value = 100, message = "Значение температуры не может быть больше чем 100")
    private Double value;

    @NotNull(message = "Поле raining не может быть пустым")
    private Boolean raining;

    @NotNull(message = "Поле sensor не может быть пустым")
    private SensorDTO sensor;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean isRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
