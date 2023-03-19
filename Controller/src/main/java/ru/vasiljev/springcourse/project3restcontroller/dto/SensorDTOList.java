package ru.vasiljev.springcourse.project3restcontroller.dto;

import java.util.List;

public class SensorDTOList {
    private List<SensorDTO> sensors;

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }
}
