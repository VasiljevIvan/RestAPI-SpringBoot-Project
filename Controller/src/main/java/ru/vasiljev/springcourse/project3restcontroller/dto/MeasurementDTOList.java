package ru.vasiljev.springcourse.project3restcontroller.dto;

import java.util.List;

public class MeasurementDTOList {
    private List<MeasurementDTO> measurements;

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }
}
