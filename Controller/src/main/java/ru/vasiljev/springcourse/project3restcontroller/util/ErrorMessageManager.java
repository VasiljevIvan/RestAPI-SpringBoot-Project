package ru.vasiljev.springcourse.project3restcontroller.util;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
public class ErrorMessageManager {
    public String getErrorMessage (BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors)
            errorMessage.append(error.getField()).append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        return errorMessage.toString();
    }
}
