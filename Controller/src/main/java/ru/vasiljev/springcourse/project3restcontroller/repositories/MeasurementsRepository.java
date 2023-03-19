package ru.vasiljev.springcourse.project3restcontroller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vasiljev.springcourse.project3restcontroller.models.Measurement;

import java.util.List;

public interface MeasurementsRepository extends JpaRepository <Measurement, Integer> {
    @Query("SELECT m FROM Measurement m WHERE m.raining = true")
    List<Measurement> findRainyDays();
}
