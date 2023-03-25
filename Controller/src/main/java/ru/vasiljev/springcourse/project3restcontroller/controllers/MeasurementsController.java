package ru.vasiljev.springcourse.project3restcontroller.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementDTO;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementDTOList;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementGraphDTO;
import ru.vasiljev.springcourse.project3restcontroller.services.MeasurementsService;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorResponse;
import ru.vasiljev.springcourse.project3restcontroller.util.MeasurementNotCreatedException;

import javax.validation.Valid;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;

    public MeasurementsController(MeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> saveMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                      BindingResult bindingResult) {
        measurementsService.save(measurementDTO,bindingResult,false);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/addwithrandomtime")
    public ResponseEntity<HttpStatus> saveRandomMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                            BindingResult bindingResult) {
        measurementsService.save(measurementDTO,bindingResult,true);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public MeasurementDTOList showMeasurements() {
        return measurementsService.findAll();
    }

    @GetMapping("/rainyDaysCount")
    public Integer showRainyDaysCount() {
        return measurementsService.findRainyDays();
    }

    @GetMapping("/showGraph")
    public MeasurementGraphDTO showGraph() {
        return measurementsService.getGraphData();
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementNotCreatedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
