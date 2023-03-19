package ru.vasiljev.springcourse.project3restcontroller;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@SpringBootApplication
public class Project3RestControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Project3RestControllerApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
