package ru.vasiljev.springcourse.project3restcontroller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vasiljev.springcourse.project3restcontroller.models.Measurement;
import ru.vasiljev.springcourse.project3restcontroller.services.SensorsService;

@Component
public class MeasurementValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public MeasurementValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Measurement.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Measurement measurement = (Measurement) o;

        if(measurement.getSensor()==null)
            errors.rejectValue("sensor","","Поле sensor не может быть пустым");

        if (sensorsService.findByName(measurement.getSensor().getName()).isEmpty())
            errors.rejectValue("sensor","","Сенсор не зарегистрирован в базе данных");
    }
}