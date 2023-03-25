package ru.vasiljev.springcourse.project3restcontroller.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementDTO;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementDTOList;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementGraphDTO;
import ru.vasiljev.springcourse.project3restcontroller.models.Measurement;
import ru.vasiljev.springcourse.project3restcontroller.repositories.MeasurementsRepository;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorMessageManager;
import ru.vasiljev.springcourse.project3restcontroller.util.ErrorResponse;
import ru.vasiljev.springcourse.project3restcontroller.util.MeasurementNotCreatedException;
import ru.vasiljev.springcourse.project3restcontroller.util.MeasurementValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;
    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;
    private final ErrorMessageManager errorMessageManager;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsService sensorsService, ModelMapper modelMapper, MeasurementValidator measurementValidator, ErrorMessageManager errorMessageManager) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
        this.errorMessageManager = errorMessageManager;
    }

    @Transactional
    public void save(MeasurementDTO measurementDTO, BindingResult bindingResult, Boolean isRandom) {
        Measurement measurement = convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurement, bindingResult);
        if (bindingResult.hasErrors())
            throw new MeasurementNotCreatedException(errorMessageManager.getErrorMessage(bindingResult));
        measurement.setSensor(sensorsService.findByName(measurement.getSensor().getName()).get());
        if (!isRandom)
            measurement.setCreatedAt(LocalDateTime.now());
        else
            measurement.setCreatedAt(LocalDateTime.of(LocalDate.now()
                    .minusDays((int) (Math.random() * 12000)), LocalTime.now().minusSeconds((int) (Math.random() * (86400)))));
        measurementsRepository.save(measurement);
    }

    public MeasurementDTOList findAll() {
        MeasurementDTOList measurements = new MeasurementDTOList();
        measurements.setMeasurements(measurementsRepository.findAll().stream()
                .map(this::convertToMeasurementDTO).collect(Collectors.toList()));
        return measurements;
    }

    public Integer findRainyDays() {
        List<Measurement> measurements = measurementsRepository.findRainyDays();
        Set<LocalDate> rainyDays = new HashSet<>();
        for (Measurement measurement : measurements)
            if (measurement.getRaining()) rainyDays.add(measurement.getCreatedAt().toLocalDate());
        return rainyDays.size();
    }

    public MeasurementGraphDTO getGraphData() {
        List<Measurement> measurements = measurementsRepository.findAll();
        Map<Date, Double> dateValueMap = new TreeMap<>();
        for (Measurement measurement : measurements) {
            dateValueMap.put(Date.from(measurement.getCreatedAt().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()), measurement.getValue());
        }
        MeasurementGraphDTO measurementGraphDTO = new MeasurementGraphDTO();
        measurementGraphDTO.setDates(new ArrayList<>(dateValueMap.keySet()));
        measurementGraphDTO.setValues(new ArrayList<>(dateValueMap.values()));
        return measurementGraphDTO;
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
