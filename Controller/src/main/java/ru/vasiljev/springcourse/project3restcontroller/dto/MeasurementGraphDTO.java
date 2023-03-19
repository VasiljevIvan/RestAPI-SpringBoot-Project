package ru.vasiljev.springcourse.project3restcontroller.dto;

import java.util.Date;
import java.util.List;

public class MeasurementGraphDTO {
    private List<Date> dates;
    private List<Double> values;

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }
}
