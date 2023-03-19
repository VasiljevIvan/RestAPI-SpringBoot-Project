package ru.vasiljev.springcourse.project3restcontroller.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementDTO;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementDTOList;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementGraphDTO;
import ru.vasiljev.springcourse.project3restcontroller.models.Measurement;
import ru.vasiljev.springcourse.project3restcontroller.services.MeasurementsService;
import ru.vasiljev.springcourse.project3restcontroller.util.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;
    private final ErrorMessageManager errorMessageManager;

    public MeasurementsController(MeasurementsService measurementsService,
                                  MeasurementValidator measurementValidator,
                                  ModelMapper modelMapper,
                                  ErrorMessageManager errorMessageManager) {
        this.measurementsService = measurementsService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
        this.errorMessageManager = errorMessageManager;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> saveMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                      BindingResult bindingResult) {
        Measurement measurement = convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurement, bindingResult);
        if (bindingResult.hasErrors())
            throw new MeasurementNotCreatedException(errorMessageManager.getErrorMessage(bindingResult));
        measurementsService.save(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/addwithrandomtime")
    public ResponseEntity<HttpStatus> saveRandomMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                            BindingResult bindingResult) {
        Measurement measurement = convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurement, bindingResult);
        if (bindingResult.hasErrors())
            throw new MeasurementNotCreatedException(errorMessageManager.getErrorMessage(bindingResult));
        measurementsService.saveRandomTimestamp(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public MeasurementDTOList showMeasurements() {
        MeasurementDTOList measurements = new MeasurementDTOList();
        measurements.setMeasurements(measurementsService.findAll().stream()
                .map(this::convertToMeasurementDTO).collect(Collectors.toList()));
        return measurements;
    }

    @GetMapping("/rainyDaysCount")
    public Integer showRainyDaysCount() {
        return measurementsService.findRainyDays();
    }

    @GetMapping("/showGraph")
    public MeasurementGraphDTO showGraph() {
        return measurementsService.getGraphData();
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
