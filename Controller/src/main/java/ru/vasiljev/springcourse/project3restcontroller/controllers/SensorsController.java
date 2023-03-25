package ru.vasiljev.springcourse.project3restcontroller.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vasiljev.springcourse.project3restcontroller.dto.SensorDTO;
import ru.vasiljev.springcourse.project3restcontroller.dto.SensorDTOList;
import ru.vasiljev.springcourse.project3restcontroller.services.SensorsService;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorResponse;
import ru.vasiljev.springcourse.project3restcontroller.util.SensorNotCreatedException;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final SensorsService sensorsService;

    @Autowired
    public SensorsController(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        sensorsService.save(sensorDTO, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public SensorDTOList showMeasurements() {
        return sensorsService.findAll();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
