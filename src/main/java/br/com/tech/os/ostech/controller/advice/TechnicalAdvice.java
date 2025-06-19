package br.com.tech.os.ostech.controller.advice;

import br.com.tech.os.ostech.exception.InvalidNameTechnicalAlreadyExistsException;
import br.com.tech.os.ostech.exception.InvalidTechnicalFieldException;
import br.com.tech.os.ostech.exception.InvalidTechnicalIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TechnicalAdvice {

    @ExceptionHandler(InvalidNameTechnicalAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> InvalidNameTechnicalAlreadyExistsException(InvalidNameTechnicalAlreadyExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidTechnicalIdException.class)
    public ResponseEntity<Map<String, String>> InvalidTechnicalIdException(InvalidTechnicalIdException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidTechnicalFieldException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTechnicalFieldException(InvalidTechnicalFieldException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

}
