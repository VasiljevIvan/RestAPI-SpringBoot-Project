package ru.vasiljev.springcourse.project3restcontroller.repositories;

import ru.vasiljev.springcourse.project3restcontroller.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorsRepository extends JpaRepository <Sensor, Integer> {
    Optional<Sensor> findByName(String name);
}
