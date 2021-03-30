package com.solactive.challenge.solactivetickvalues.exceptionhandler;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = {RestController.class})
public class CustomExceptionHandler extends ExceptionHandlerExceptionResolver {



    @org.springframework.web.bind.annotation.ExceptionHandler(value
            = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleExceptions(
            final Exception ex, final HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse();
        response.setError( ex.getMessage());
        response.callerURL(request.getRequestURI());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

