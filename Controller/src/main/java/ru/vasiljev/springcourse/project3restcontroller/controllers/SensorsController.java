package ru.vasiljev.springcourse.project3restcontroller.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vasiljev.springcourse.project3restcontroller.dto.SensorDTO;
import ru.vasiljev.springcourse.project3restcontroller.dto.SensorDTOList;
import ru.vasiljev.springcourse.project3restcontroller.models.Sensor;
import ru.vasiljev.springcourse.project3restcontroller.services.SensorsService;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorMessageManager;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorResponse;
import ru.vasiljev.springcourse.project3restcontroller.util.SensorNotCreatedException;
import ru.vasiljev.springcourse.project3restcontroller.util.SensorValidator;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;
    private final ErrorMessageManager errorMessageManager;

    @Autowired
    public SensorsController(SensorsService sensorsService,
                             SensorValidator sensorValidator,
                             ModelMapper modelMapper, ErrorMessageManager errorMessageManager) {
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
        this.errorMessageManager = errorMessageManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        Sensor sensor = convertToSensor(sensorDTO);
        sensorValidator.validate(sensor, bindingResult);
        if (bindingResult.hasErrors())
            throw new SensorNotCreatedException(errorMessageManager.getErrorMessage(bindingResult));
        sensorsService.save(sensor);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public SensorDTOList showMeasurements() {
        SensorDTOList sensors = new SensorDTOList();
        sensors.setSensors(sensorsService.findAll().stream()
                .map(this::convertToSensorDTO).collect(Collectors.toList()));
        return sensors;
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
