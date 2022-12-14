package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.exceptions.LicencePlateException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingAllocationException;
import com.switchfully.patekes.parksharkpatekes.exceptions.ParkingLotException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MemberException.class)
    protected void memberException(MemberException ex, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(ParkingLotException.class)
    protected void parkingLotException(ParkingLotException ex, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(LicencePlateException.class)
    protected void licencePlateException(LicencePlateException ex, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(ParkingAllocationException.class)
    protected void parkingAllocationException(ParkingAllocationException ex, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }
}
