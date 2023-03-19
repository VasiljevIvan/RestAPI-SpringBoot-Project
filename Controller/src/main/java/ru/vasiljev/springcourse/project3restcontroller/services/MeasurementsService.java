package ru.vasiljev.springcourse.project3restcontroller.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vasiljev.springcourse.project3restcontroller.dto.MeasurementGraphDTO;
import ru.vasiljev.springcourse.project3restcontroller.models.Measurement;
import ru.vasiljev.springcourse.project3restcontroller.repositories.MeasurementsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsService sensorsService) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsService = sensorsService;
    }

    @Transactional
    public void save(Measurement measurement) {
        measurement.setSensor(sensorsService.findByName(measurement.getSensor().getName()).get());
        measurement.setCreatedAt(LocalDateTime.now());
        measurementsRepository.save(measurement);
    }

    @Transactional
    public void saveRandomTimestamp(Measurement measurement) {
        measurement.setSensor(sensorsService.findByName(measurement.getSensor().getName()).get());
        measurement.setCreatedAt(LocalDateTime.of(LocalDate.now()
                .minusDays((int) (Math.random() * 12000)), LocalTime.now()
                .minusSeconds((int) (Math.random() * (86400)))));
        measurementsRepository.save(measurement);
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public Integer findRainyDays() {
        List<Measurement> measurements = measurementsRepository.findRainyDays();
        Set<LocalDate> rainyDays = new HashSet<>();
        for (Measurement measurement : measurements)
            if (measurement.getRaining())
                rainyDays.add(measurement.getCreatedAt().toLocalDate());
        return rainyDays.size();
    }

    public MeasurementGraphDTO getGraphData() {
        List<Measurement> measurements = measurementsRepository.findAll();
        Map<Date, Double> dateValueMap = new TreeMap<>();
        for (Measurement measurement : measurements) {
            dateValueMap.put(Date.from(measurement.getCreatedAt()
                            .toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    measurement.getValue());
        }
        MeasurementGraphDTO measurementGraphDTO = new MeasurementGraphDTO();
        measurementGraphDTO.setDates(new ArrayList<>(dateValueMap.keySet()));
        measurementGraphDTO.setValues(new ArrayList<>(dateValueMap.values()));
        return measurementGraphDTO;
    }
}
