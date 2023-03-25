package ru.vasiljev.springcourse.project3restcontroller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vasiljev.springcourse.project3restcontroller.models.Sensor;
import ru.vasiljev.springcourse.project3restcontroller.repositories.SensorsRepository;
import ru.vasiljev.springcourse.project3restcontroller.services.SensorsService;

@Component
public class SensorValidator implements Validator {
    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorValidator(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Sensor.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Sensor sensor = (Sensor) o;
        if (sensorsRepository.findByName(sensor.getName()).isPresent())
            errors.rejectValue("name","","Сенсор с таким именем уже существует");
    }
}
