package ru.vasiljev.springcourse.project3restcontroller.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.vasiljev.springcourse.project3restcontroller.dto.SensorDTO;
import ru.vasiljev.springcourse.project3restcontroller.dto.SensorDTOList;
import ru.vasiljev.springcourse.project3restcontroller.models.Sensor;
import ru.vasiljev.springcourse.project3restcontroller.repositories.SensorsRepository;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorMessageManager;
import ru.vasiljev.springcourse.project3restcontroller.util.SensorNotCreatedException;
import ru.vasiljev.springcourse.project3restcontroller.util.SensorValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SensorsService {
    private final SensorsRepository sensorsRepository;
    private final SensorValidator sensorValidator;
    private final ErrorMessageManager errorMessageManager;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsService(SensorsRepository sensorsRepository, SensorValidator sensorValidator, ErrorMessageManager errorMessageManager, ModelMapper modelMapper) {
        this.sensorsRepository = sensorsRepository;
        this.sensorValidator = sensorValidator;
        this.errorMessageManager = errorMessageManager;
        this.modelMapper = modelMapper;
    }

    public SensorDTOList findAll() {
        SensorDTOList sensors = new SensorDTOList();
        sensors.setSensors(sensorsRepository.findAll().stream()
                .map(this::convertToSensorDTO).collect(Collectors.toList()));
        return sensors;
    }

    public Optional<Sensor> findByName(String name) {
        return sensorsRepository.findByName(name);
    }

    @Transactional
    public void save(SensorDTO sensorDTO, BindingResult bindingResult) {
        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, bindingResult);
        if (bindingResult.hasErrors())
            throw new SensorNotCreatedException(errorMessageManager.getErrorMessage(bindingResult));
        sensorsRepository.save(sensor);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
